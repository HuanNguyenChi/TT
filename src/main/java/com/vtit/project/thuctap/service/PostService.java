package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreatePostRequest;
import com.vtit.project.thuctap.dto.request.UpdatePostRequest;
import com.vtit.project.thuctap.dto.response.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostDTO> findPosts(Pageable pageable, Long userId);
    PostDTO save(CreatePostRequest request);
    PostDTO findById(Long id);
    PostDTO update(UpdatePostRequest request);
    void delete(Long id);
}
