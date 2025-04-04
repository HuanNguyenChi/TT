package com.vtit.project.thuctap.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String code;
    private String title;
    private String author;
    private String description;
    private Integer quantity;
    private Integer publishedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isActive;
    private Boolean isDeleted;

}
