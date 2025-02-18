package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CombinedDto2;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.exception.BusinessException;
import com.bootcamp.sb.demo_sb_bc_forum.exception.SysCode;
import com.bootcamp.sb.demo_sb_bc_forum.service.CombinedInfoService;

@Service
public class CombinedInfoServiceImpl implements CombinedInfoService{
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public List<CombinedDto> getCombinedInfo() {
        String usersUrl = "https://jsonplaceholder.typicode.com/users";
        String postsUrl = "https://jsonplaceholder.typicode.com/posts";
        String commentsUrl = "https://jsonplaceholder.typicode.com/comments";

        // store the data with a list for each URL page
        List<UserDto> userDtos = 
            Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

        List<PostDto> postDtos = 
            Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));

        List<CommentDto> commentDtos = 
            Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));

        Map<Long, List<PostDto>> userPostsMap = new HashMap<>();
        for (PostDto postDto : postDtos) {
            userPostsMap.computeIfAbsent(postDto.getUserId(), k -> new ArrayList<>()).add(postDto);
            // k -> new ArrayList<>(): This is the mapping function used by computeIfAbsent.
            // It takes the key (k, which is the userId) and returns a new ArrayList<PostDto>.
            // Essentially, this is creating a new list to hold posts if one doesn't already exist for that userId.
            // .add(postDto): After ensuring that there is a list for the userId, this method call adds the current postDto object to that list.
        }

        Map<Long, List<CommentDto>> postCommentsMap = new HashMap<>();
        for (CommentDto commentDto : commentDtos) {
            postCommentsMap.computeIfAbsent(commentDto.getPostId(), k -> new ArrayList<>()).add(commentDto);
        }

        List<CombinedDto> combinedDtos = new ArrayList<>();
        for (UserDto userDto : userDtos) {
            CombinedDto combinedDto = CombinedDto.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .address(convertAddress(userDto.getAddress()))
                .phone(userDto.getPhone())
                .website(userDto.getWebsite())
                .company(convertCompany(userDto.getCompany()))
                .build();

            List<CombinedDto.Post> combinedPosts = new ArrayList<>();
            for (PostDto postDto : userPostsMap.getOrDefault(userDto.getId(), new ArrayList<>())) {
                List<CombinedDto.Post.Comment> combinedComments = new ArrayList<>();
                for (CommentDto commentDto : postCommentsMap.getOrDefault(postDto.getId(), new ArrayList<>())) {
                    CombinedDto.Post.Comment combinedComment = CombinedDto.Post.Comment.builder()
                        .id(commentDto.getId())
                        .name(commentDto.getName())
                        .email(commentDto.getEmail())
                        .body(commentDto.getBody())
                        .build();
                    combinedComments.add(combinedComment);
                }
                CombinedDto.Post combinedPost = CombinedDto.Post.builder()
                    .id(postDto.getId())
                    .title(postDto.getTitle())
                    .body(postDto.getBody())
                    .comments(combinedComments)
                    .build();
                combinedPosts.add(combinedPost);
            }
            combinedDto.setPosts(combinedPosts);
            combinedDtos.add(combinedDto);
        }
        return combinedDtos;

    }

    private CombinedDto.Address convertAddress(UserDto.Address userAddress) {
        if (userAddress == null) {
            return null;
        }
        return CombinedDto.Address.builder()
            .street(userAddress.getStreet())
            .suite(userAddress.getSuite())
            .city(userAddress.getCity())
            .zipcode(userAddress.getZipcode())
            .geo(CombinedDto.Address.Geo.builder()
                .lat(userAddress.getGeo().getLat())
                .lng(userAddress.getGeo().getLng())
                .build())
            .build();
    }

    private CombinedDto.Company convertCompany(UserDto.Company userCompany) {
        if (userCompany == null) {
            return null;
        }
        return CombinedDto.Company.builder()
            .name(userCompany.getName())
            .catchPhrase(userCompany.getCatchPhrase())
            .bs(userCompany.getBs())
            .build();
    }

    public List<CombinedDto2> getAllCommentsByUserId(String id) {
        Long userId;
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.INVALID_INPUT);
        }
        
        String usersUrl = "https://jsonplaceholder.typicode.com/users";
        String postsUrl = "https://jsonplaceholder.typicode.com/posts";
        String commentsUrl = "https://jsonplaceholder.typicode.com/comments";

        List<UserDto> userDtos = 
            Arrays.asList(this.restTemplate.getForObject(usersUrl, UserDto[].class));

        List<PostDto> postDtos = 
            Arrays.asList(this.restTemplate.getForObject(postsUrl, PostDto[].class));

        List<CommentDto> commentDtos = 
            Arrays.asList(this.restTemplate.getForObject(commentsUrl, CommentDto[].class));

        Map<Long, List<PostDto>> userPostsMap = new HashMap<>();
        for (PostDto postDto : postDtos) {
            userPostsMap.computeIfAbsent(postDto.getUserId(), k -> new ArrayList<>()).add(postDto);
        }
    
        Map<Long, List<CommentDto>> postCommentsMap = new HashMap<>();
        for (CommentDto commentDto : commentDtos) {
            postCommentsMap.computeIfAbsent(commentDto.getPostId(), k -> new ArrayList<>()).add(commentDto);
        }
    
        List<CombinedDto2> combinedDtos2 = new ArrayList<>();
        for (UserDto userDto : userDtos) {
            CombinedDto2 combinedDto2 = CombinedDto2.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .build();
    
            // List<CombinedDto2.Post> combinedPosts2 = new ArrayList<>();
            List<CombinedDto2.Post.Comment> combinedComments2 = new ArrayList<>();
            for (PostDto postDto : userPostsMap.getOrDefault(userDto.getId(), new ArrayList<>())) {
                // List<CombinedDto2.Post.Comment> combinedComments2 = new ArrayList<>();
                for (CommentDto commentDto : postCommentsMap.getOrDefault(postDto.getId(), new ArrayList<>())) {
                    CombinedDto2.Post.Comment combinedComment2 = CombinedDto2.Post.Comment.builder()
                        .name(commentDto.getName())
                        .email(commentDto.getEmail())
                        .body(commentDto.getBody())
                        .build();
                    combinedComments2.add(combinedComment2);
                    }
                    // CombinedDto2.Post combinedPost2 = CombinedDto2.Post.builder()
                    //     .comments(combinedComments2)
                    //     .build();
                    // combinedPosts2.add(combinedPost2);
                }
                // combinedDto2.setPosts(combinedPosts2);
                combinedDto2.setComments(combinedComments2);
                combinedDtos2.add(combinedDto2);
        }

        List<CombinedDto2> filteredDtos = combinedDtos2.stream()
            .filter(e -> e.getId().equals(userId))
            .collect(Collectors.toList());

        // Throw BusinessException if no matching ID is found
        if (filteredDtos.isEmpty()) {
            throw new BusinessException(SysCode.ID_NOT_FOUND);
        }

        return filteredDtos;
    }

}
