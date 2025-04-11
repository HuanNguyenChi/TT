package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.request.CreatePostRequest;
import com.vtit.project.thuctap.dto.request.UpdatePostRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.PostDTO;
import com.vtit.project.thuctap.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ApiResponse<Page<?>> getPostsByUserId(@RequestParam("userId") Long userId,
                                                 @PageableDefault(page = 0, size = 10)Pageable pageable){
        return ApiResponse.<Page<?>>builder()
                .result(postService.findPosts(pageable, userId))
                .build();

    }

    @GetMapping("/detail")
    public ApiResponse<?> getPostDetail(@RequestParam("postId") Long postId){
        return ApiResponse.<PostDTO>builder()
                .result(postService.findById(postId))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<?> createPost(@RequestBody CreatePostRequest request) {
        return ApiResponse.<PostDTO>builder()
                .result(postService.save(request))
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<?> updatePost(@RequestBody UpdatePostRequest request) {
        return ApiResponse.<PostDTO>builder()
                .result(postService.update(request))
                .build();
    }
    @DeleteMapping("/delete")
    public ApiResponse<?> deletePost(@RequestParam("postId") Long postId){
        postService.delete(postId);
        return ApiResponse.<String>builder()
                .result("Post has been delete")
                .build();
    }
}
