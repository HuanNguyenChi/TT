package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.dto.request.CreateCategoryRequest;
import com.vtit.project.thuctap.dto.request.ListCategoryRequest;
import com.vtit.project.thuctap.dto.request.UpdateCategoryRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.CategoryDTO;
import com.vtit.project.thuctap.dto.response.ResponseMessage;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/library/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping()
    public ApiResponse<?> findCategoryFilter(@RequestBody ListCategoryRequest request,
                                             @PageableDefault(page = 0, size = 10, sort = "code", direction = Sort.Direction.ASC)Pageable pageable){
        return ApiResponse.<Page<?>>builder()
                .result(categoryService.findCategoryFilter(request, pageable))
                .build();
    }

    @GetMapping("/detail")
    public ApiResponse<?> getCategoryDetail(@RequestParam("id") Long id){
        return ApiResponse.<CategoryDTO>builder()
                .result(categoryService.findCategoryById(id))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<?> createCategory(@RequestBody CreateCategoryRequest request){
        return ApiResponse.<CategoryDTO>builder()
                .result(categoryService.CreateCategory(request))
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<?> updateCategory(@RequestBody UpdateCategoryRequest request){
        return ApiResponse.<CategoryDTO>builder()
                .result(categoryService.updateCategory(request))
                .build();
    }
    @DeleteMapping("/delete")
    public ApiResponse<?> deleteCategory(@RequestParam("ids") List<Long> ids){
        categoryService.deleteCategory(ids);
        return ApiResponse.<String>builder()
                .result("Category has been deleted")
                .build();
    }

}
