package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateCategoryRequest;
import com.vtit.project.thuctap.dto.request.ListCategoryRequest;
import com.vtit.project.thuctap.dto.request.UpdateCategoryRequest;
import com.vtit.project.thuctap.dto.response.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDTO CreateCategory(CreateCategoryRequest request);
    Page<CategoryDTO> findCategoryFilter(ListCategoryRequest request, Pageable pageable);
    CategoryDTO findCategoryById(Long id);
    CategoryDTO updateCategory(UpdateCategoryRequest request);
    void deleteCategory(List<Long> ids);
}
