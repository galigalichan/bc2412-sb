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
import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.UserDto;
import com.bootcamp.sb.demo_sb_bc_forum.dto.mapper.CommentDtoMapper;
import com.bootcamp.sb.demo_sb_bc_forum.entity.AddressEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CompanyEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.GeoEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.PostEntity;
import com.bootcamp.sb.demo_sb_bc_forum.entity.UserEntity;
import com.bootcamp.sb.demo_sb_bc_forum.repository.CommentRepository;
import com.bootcamp.sb.demo_sb_bc_forum.repository.PostRepository;
import com.bootcamp.sb.demo_sb_bc_forum.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    public List<CommentDto> getCommentsFromUrl() {
        String url = "https://jsonplaceholder.typicode.com/comments";

        List<CommentDto> commentDtos = 
            Arrays.asList(this.restTemplate.getForObject(url, CommentDto[].class));

        List<CommentDto> filteredCommentDtos = commentDtos.stream().map(e -> CommentDtoMapper.map(e)).collect(Collectors.toList());

        return filteredCommentDtos;
    }

    public ApiResp<List<CommentDto>> getComments() {
        List<CommentEntity> comments = commentRepository.findAll();
        List<CommentDto> commentDtos = comments.stream()
            .map(comment -> new CommentDto(comment.getId(), comment.getName(), comment.getEmail(), comment.getBody(), comment.getPostId()))
            .collect(Collectors.toList());
    
        return ApiResp.<List<CommentDto>>builder().syscode(SysCode.SUCCESS).data(commentDtos).build();
    }

    @Override
    public ApiResp<List<CommentDto>> getCommentsByPostId(String id) {
        Long postId;

        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        List<CommentEntity> comments = commentRepository.findAll();

        List<CommentDto> targetComments = comments.stream().filter(e -> e.getPostId().equals(postId)).map(comment -> new CommentDto(comment.getId(), comment.getName(), comment.getEmail(), comment.getBody(), comment.getPostId())).collect(Collectors.toList());

        if (targetComments.isEmpty()) {
            return globalExceptionHandler.handleCommentNotFoundException();
        }
        
        return ApiResp.<List<CommentDto>>builder().syscode(SysCode.SUCCESS).data(targetComments).build();
    }

    @Override
    public ApiResp<CommentDto> addComment(String id, CommentDto commentDto) {
        Long postId;

        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        // Retrieve the PostEntity by postId
        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new BusinessException(SysCode.API_UNAVAILABLE)); // Handle post not found

        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        commentRepository.save(comment);

        // Create a CommentDto with postId
        CommentDto responseDto = new CommentDto(comment.getId(), comment.getName(), comment.getEmail(), comment.getBody(), comment.getPostId());

        return ApiResp.<CommentDto>builder().syscode(SysCode.SUCCESS).data(responseDto).build();
    }

    @Override
    public ApiResp<CommentEntity> updateComment(String id, CommentDto commentDto) {
        Long commentId;

        try {
            commentId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new BusinessException(SysCode.API_UNAVAILABLE);
        }

        CommentEntity commentToBeUpdated = this.commentRepository.findById(commentId).orElseThrow(() -> new BusinessException(SysCode.API_UNAVAILABLE));
    
            // Update the comment body
            commentToBeUpdated.setBody(commentDto.getBody());
    
            // Save the updated comment entity
            CommentEntity updatedComment = this.commentRepository.save(commentToBeUpdated);

            return ApiResp.<CommentEntity>builder().syscode(SysCode.SUCCESS).data(updatedComment).build();
    }

}
