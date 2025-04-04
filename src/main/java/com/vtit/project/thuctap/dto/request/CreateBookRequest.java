package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateBookRequest {
    private String code;
    private String title;
    private String author;
    private Integer quantity;
    private String description;
    private Integer publishedAt;
    private List<Long> categoryList;
}
