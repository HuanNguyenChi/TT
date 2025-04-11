package com.vtit.project.thuctap.dto.request;

import com.vtit.project.thuctap.dto.response.CommentDTO;
import com.vtit.project.thuctap.dto.response.UserDTO;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class CreatePostRequest {
    private String title;
    private String content;
    private int createdBy;
    private int updatedBy;

}
