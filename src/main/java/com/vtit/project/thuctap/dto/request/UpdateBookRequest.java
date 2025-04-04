package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateBookRequest {
    private Long id;
    private String code;
    private String title;
    private String author;
    private String description;
    private Integer publishedAt;
    private Integer quantity;
    private boolean isActive;
    private boolean isDeleted;
    private List<Long> categoryList;
}
