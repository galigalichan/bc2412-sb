package com.bootcamp.sb.demo_sb_bc_forum.config;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.AddressEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CompanyEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.GeoEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;
import com.bootcamp.sb.demo_sb_bc_forum.repository.AddressRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.CommentRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.CompanyRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.GeoRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.PostRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.UserRepository;

@Component // bean
@Transactional // Ensures atomicity
public class PreServerStartConfig implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(PreServerStartConfig.class);
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private GeoRepository geoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        fetchAndSaveUsers();
        fetchAndSavePosts();
        fetchAndSaveComments();
    }

    private void fetchAndSaveUsers() {
        String usersUrl = "https://jsonplaceholder.typicode.com/users";
        ResponseEntity<UserDto[]> userResponse = restTemplate.getForEntity(usersUrl, UserDto[].class);
        UserDto[] users = userResponse.getBody();
    
        if (users != null) {
            for (UserDto userDto : users) {
                // Create a new UserEntity without setting the ID
                UserEntity userEntity = new UserEntity();
                // userEntity.setId(userDto.getId());
                userEntity.setName(userDto.getName());
                userEntity.setUsername(userDto.getUsername());
                userEntity.setEmail(userDto.getEmail());
                userEntity.setPhone(userDto.getPhone());
                userEntity.setWebsite(userDto.getWebsite());
    
                // First, save UserEntity to ensure it has an ID
            try {
                userRepository.save(userEntity);
            } catch (StaleObjectStateException e) {
                logger.error("Failed to save user {}: {}", userEntity.getName(), e.getMessage());
                // Handle the exception (e.g., retry logic or user notification)
            }
    
                // Address
                UserDto.Address addressDto = userDto.getAddress();
                if (addressDto != null) {
                    AddressEntity addressEntity = new AddressEntity();
                    addressEntity.setStreet(addressDto.getStreet());
                    addressEntity.setSuite(addressDto.getSuite());
                    addressEntity.setCity(addressDto.getCity());
                    addressEntity.setZipcode(addressDto.getZipcode());
                    addressEntity.setUser(userEntity); // Set user reference
    
                    // Save AddressEntity after UserEntity is saved
                    addressRepository.save(addressEntity);
    
                    // Geo
                    UserDto.Address.Geo geoDto = addressDto.getGeo();
                    if (geoDto != null) {
                        GeoEntity geoEntity = new GeoEntity();
                        geoEntity.setLat(geoDto.getLat());
                        geoEntity.setLng(geoDto.getLng());
                        geoEntity.setAddress(addressEntity); // Set address reference
    
                        // Save GeoEntity
                        geoRepository.save(geoEntity);
                    }
                }
    
                // Company
                UserDto.Company companyDto = userDto.getCompany();
                if (companyDto != null) {
                    CompanyEntity companyEntity = new CompanyEntity();
                    companyEntity.setName(companyDto.getName());
                    companyEntity.setCatchPhrase(companyDto.getCatchPhrase());
                    companyEntity.setBs(companyDto.getBs());
                    companyEntity.setUser(userEntity); // Set user reference
    
                    // Save CompanyEntity
                    companyRepository.save(companyEntity);
                }
    
                logger.info("Saved user: {}", userEntity.getName());
            }
        }
    }

    private void fetchAndSavePosts() {
        String postsUrl = "https://jsonplaceholder.typicode.com/posts";
        ResponseEntity<PostDto[]> postResponse = restTemplate.getForEntity(postsUrl, PostDto[].class);
        PostDto[] posts = postResponse.getBody();
    
        if (posts != null) {
            for (PostDto postDto : posts) {
                try {
                    // Refetch the UserEntity to ensure it's managed and up-to-date
                    UserEntity userEntity = userRepository.findById(postDto.getUserId()).orElse(null);
                    
                    if (userEntity != null) {
                        PostEntity postEntity = new PostEntity();
                        // postEntity.setId(postDto.getId());
                        postEntity.setTitle(postDto.getTitle());
                        postEntity.setBody(postDto.getBody());
                        postEntity.setUser(userEntity); // Set user reference
                        
                        // Save PostEntity
                        postRepository.save(postEntity);
                        logger.info("Saved post: {}", postEntity.getTitle());
                    } else {
                        logger.warn("User with ID {} not found for post {}", postDto.getUserId(), postDto.getId());
                    }
                } catch (StaleObjectStateException e) {
                    logger.error("Failed to save post {}: {}", postDto.getId(), e.getMessage());
                    // Handle the exception (e.g., retry logic or user notification)
                }
            }
        }
    }

    private void fetchAndSaveComments() {
        String commentsUrl = "https://jsonplaceholder.typicode.com/comments";
        ResponseEntity<CommentDto[]> commentResponse = restTemplate.getForEntity(commentsUrl, CommentDto[].class);
        CommentDto[] comments = commentResponse.getBody();

        if (comments != null) {
            for (CommentDto commentDto : comments) {
                CommentEntity commentEntity = new CommentEntity();
                // commentEntity.setId(commentDto.getId());
                commentEntity.setName(commentDto.getName());
                commentEntity.setEmail(commentDto.getEmail());
                commentEntity.setBody(commentDto.getBody());

                // Find the post by ID
                PostEntity postEntity = postRepository.findById(commentDto.getPostId()).orElse(null);
                if (postEntity != null) {
                    commentEntity.setPost(postEntity); // Set post reference
                    commentRepository.save(commentEntity);
                    logger.info("Saved comment: {}", commentEntity.getName());
                }
            }
        }
    }
}

