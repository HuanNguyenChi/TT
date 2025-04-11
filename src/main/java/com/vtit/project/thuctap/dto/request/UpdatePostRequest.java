package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UpdatePostRequest {
    private Long id;
    private String title;
    private String content;
    private Timestamp publishedAt;
    private Boolean isDeleted;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int createdBy;
    private int updatedBy;
}
