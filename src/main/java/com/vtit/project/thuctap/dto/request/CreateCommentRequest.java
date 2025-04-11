package com.vtit.project.thuctap.dto.request;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String content;
    private Long postId;
    private Long userId;
}
