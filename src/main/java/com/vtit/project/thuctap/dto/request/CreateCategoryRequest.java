package com.vtit.project.thuctap.dto.request;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String name;
    private String code;
    private String description;
}
