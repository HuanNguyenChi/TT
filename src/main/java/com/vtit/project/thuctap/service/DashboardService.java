package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.PostDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DashboardService {

    List<BookDTO> statisticBookByCategory(Long categoryId);
    List<PostDTO> getPostByReact(Pageable pageable);
}
