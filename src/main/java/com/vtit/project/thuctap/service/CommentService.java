package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateCommentRequest;
import com.vtit.project.thuctap.dto.request.UpdateCommentRequest;
import com.vtit.project.thuctap.dto.response.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentDTO> findCommentsByPostId(Long postId, Pageable pageable);
    CommentDTO findCommentById(Long id);
    CommentDTO save(CreateCommentRequest request);
    CommentDTO updateComment(UpdateCommentRequest request);
    void deleteComment(Long id);
}
