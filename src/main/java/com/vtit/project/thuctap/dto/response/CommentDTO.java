package com.vtit.project.thuctap.dto.response;


import lombok.Data;

import java.sql.Timestamp;

@Data

public class CommentDTO {
    private long postId;
    private Long id;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isActive;
    private Boolean isDeleted;
    private long userId;
}
