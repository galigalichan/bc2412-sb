package com.bootcamp.sb.demo_sb_bc_forum.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;

public interface CommentOperation {
    @GetMapping(value = "/jph/comments")
    List<CommentDto> getCommentsFromUrl();

    @GetMapping(value = "/api/comments")
    ApiResp<List<CommentDto>> getComments();

    @GetMapping(value = "/api/commentsbypostid")
    ApiResp<List<CommentDto>> getCommentsByPostId(@RequestParam(value = "postid") String id);

    @PostMapping(value = "/api/comment")
    ApiResp<CommentDto> addComment(@RequestParam(value = "postid") String id, @RequestBody CommentDto commentDto);

    @PatchMapping(value = "/api/comment")
    ApiResp<CommentEntity> updateComment(@RequestParam(value = "commentid") String id, @RequestBody CommentDto commentDto);

}
