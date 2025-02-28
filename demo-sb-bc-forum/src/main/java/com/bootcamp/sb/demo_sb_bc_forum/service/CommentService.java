package com.bootcamp.sb.demo_sb_bc_forum.service;

import java.util.List;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;

public interface CommentService {
    List<CommentDto> getCommentsFromUrl();

    ApiResp<List<CommentDto>> getComments();

    ApiResp<List<CommentDto>> getCommentsByPostId(String id);

    ApiResp<CommentDto> addComment(String id, CommentDto commentDto);

    ApiResp<CommentEntity> updateComment(String id, CommentDto commentDto);

}
