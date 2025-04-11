package com.vtit.project.thuctap.dto.response;

import com.vtit.project.thuctap.entity.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Timestamp publishedAt;
    private Boolean isDeleted;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserDTO createdBy;
    private UserDTO updatedBy;
    private List<CommentDTO> commentList;
}
