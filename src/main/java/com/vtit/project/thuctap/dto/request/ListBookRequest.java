package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ListBookRequest {
    private String code;
    private String author;
    private Integer publishedAt;
    private List<Long> categoryList;
    private boolean isActive = true;
    private boolean isDeleted;
}
