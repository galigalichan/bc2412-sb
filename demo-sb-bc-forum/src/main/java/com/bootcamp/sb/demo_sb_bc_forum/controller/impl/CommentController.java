package com.bootcamp.sb.demo_sb_bc_forum.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.sb.demo_sb_bc_forum.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_bc_forum.controller.CommentOperation;
import com.bootcamp.sb.demo_sb_bc_forum.dto.CommentDto;
import com.bootcamp.sb.demo_sb_bc_forum.entity.CommentEntity;
import com.bootcamp.sb.demo_sb_bc_forum.service.impl.CommentServiceImpl;

@RestController
public class CommentController implements CommentOperation {
    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @GetMapping(value = "/jph/comments")
    public List<CommentDto> getCommentsFromUrl() {
        return this.commentServiceImpl.getCommentsFromUrl();
    }

    @GetMapping(value = "/api/comments")
    public ApiResp<List<CommentDto>> getComments() {
        return this.commentServiceImpl.getComments();
    }

    @GetMapping(value = "/api/commentsbypostid")
    public ApiResp<List<CommentDto>> getCommentsByPostId(@RequestParam(value = "postid") String id) {
        return this.commentServiceImpl.getCommentsByPostId(id);
    }

    @PostMapping(value = "/api/comment")
    public ApiResp<CommentDto> addComment(@RequestParam(value = "postid") String id, @RequestBody CommentDto commentDto) {
        return this.commentServiceImpl.addComment(id, commentDto);
    }

    @PatchMapping(value = "/api/comment")
    public ApiResp<CommentEntity> updateComment(@RequestParam(value = "commentid") String id, @RequestBody CommentDto commentDto) {
        return this.commentServiceImpl.updateComment(id, commentDto);
    }
}
