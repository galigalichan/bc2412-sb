package com.bootcamp.sb.demo_sb_bc_forum.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.BusinessException;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.GlobalExceptionHandler;
import com.bootcamp.sb.demo_sb_bc_forum.codewave.SysCode;
import com.bootcamp.sb.demo_sb_bc_forum.dto.PostDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.PostDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;
import com.bootcamp.sb.demo_sb_bc_forum.repository.CommentRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.PostRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.UserRepository;
import com.bootcamp.sb.demo_sb_bc_forum.service.PostService;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public List<PostDto> getPostsFromUrl() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        List<PostDto> postDtos = 
            Arrays.asList(this.restTemplate.getForObject(url, PostDto[].class));

        List<PostDto> filteredPostDtos = postDtos.stream().map(e -> PostDtoMapper.map(e)).collect(Collectors.toList());
            
        return filteredPostDtos;
    }

    @Override
    public ApiResp<List<PostDto>> getPosts() {
        List<PostEntity> posts = postRepository.findAll();
        List<PostDto> postDtos = posts.stream()
            .map(post -> new PostDto(post.getId(), post.getTitle(), post.getBody(), post.getUserId()))
            .collect(Collectors.toList());
    
        return ApiResp.<List<PostDto>>builder().syscode(SysCode.SUCCESS).data(postDtos).build();
    }

    @Override
    public ApiResp<List<PostDto>> getPostsByUserId(String id) {
        Long userId;

        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        List<PostEntity> posts = postRepository.findAll();

        List<PostDto> targetPosts = posts.stream().filter(e -> e.getUserId().equals(userId)).map(post -> new PostDto(post.getId(), post.getTitle(), post.getBody(), post.getUserId())).collect(Collectors.toList());

        if (targetPosts.isEmpty()) {
            return globalExceptionHandler.handlePostNotFoundException();
        }
        
        return ApiResp.<List<PostDto>>builder().syscode(SysCode.SUCCESS).data(targetPosts).build();
    }

    @Override
    public ApiResp<PostDto> addPost(String id, PostDto postDto) {
        Long userId;

        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        // Retrieve the UserEntity by userId
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(SysCode.API_UNAVAILABLE)); // Handle user not found

        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setTitle(postDto.getTitle());
        post.setBody(postDto.getBody());

        postRepository.save(post);

        // Create a PostDto with userId
        PostDto responseDto = new PostDto(post.getId(), post.getTitle(), post.getBody(), user.getId());

        return ApiResp.<PostDto>builder().syscode(SysCode.SUCCESS).data(responseDto).build();
    }

    @Override
    public ApiResp<PostEntity> deletePost(String id) {
        Long postId;

        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(SysCode.API_UNAVAILABLE));

        // Delete comments associated with the post
        List<CommentEntity> comments = commentRepository.findAll();

        List<CommentEntity> targetComments = comments.stream().filter(e -> e.getPostId().equals(postId)).collect(Collectors.toList());
        
        for (CommentEntity comment : targetComments) {
            commentRepository.delete(comment); // Delete each comment individually
        }

        postRepository.delete(post);

        return ApiResp.<PostEntity>builder().syscode(SysCode.SUCCESS).data(post).build();
    }

}
