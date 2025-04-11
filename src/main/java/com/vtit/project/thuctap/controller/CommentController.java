package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.request.CreateCommentRequest;
import com.vtit.project.thuctap.dto.request.UpdateCommentRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.CommentDTO;
import com.vtit.project.thuctap.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ApiResponse<Page<?>> getCommentsByPostId(@RequestParam("postId") Long postId, Pageable pageable){
        return ApiResponse.<Page<?>>builder()
                .result(commentService.findCommentsByPostId(postId, pageable))
                .build();
    }

    @GetMapping("/detail")
    public ApiResponse<?> getCommentDetail(@RequestParam("commentId") Long commentId){
        return ApiResponse.<CommentDTO>builder()
                .result(commentService.findCommentById(commentId))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<?> createComment(@RequestBody CreateCommentRequest request){
        return ApiResponse.<CommentDTO>builder()
                .result(commentService.save(request))
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<?> updateComment(@RequestBody UpdateCommentRequest request){
        return ApiResponse.<CommentDTO>builder()
                .result(commentService.updateComment(request))
                .build();
    }
    @DeleteMapping("/delete")
    public ApiResponse<?> deleteComment(@RequestParam("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return ApiResponse.<String>builder()
                .result("Comment has been delete")
                .build();
    }
}
