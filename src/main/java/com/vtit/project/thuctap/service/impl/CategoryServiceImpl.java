package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreateCategoryRequest;
import com.vtit.project.thuctap.dto.request.ListCategoryRequest;
import com.vtit.project.thuctap.dto.request.UpdateCategoryRequest;
import com.vtit.project.thuctap.dto.response.CategoryDTO;
import com.vtit.project.thuctap.entity.Category;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.CategoryRepository;
import com.vtit.project.thuctap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryDTO CreateCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByCode(request.getCode())) {
            throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.CATEGORY);
        }
        Category category = modelMapper.map(request, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public Page<CategoryDTO> findCategoryFilter(ListCategoryRequest request, Pageable pageable) {

        return categoryRepository.findCategoryFilter(request, pageable);
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.CATEGORY));
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.CATEGORY));
        if(!category.getCode().equals(request.getCode())) {
            if (categoryRepository.existsByCode(request.getCode())){
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.CODE);
            }
        }
        Category categoryMap = modelMapper.map(request, Category.class);
        return modelMapper.map(categoryRepository.save(categoryMap), CategoryDTO.class);
    }

    @Override
    public void deleteCategory(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }
}
