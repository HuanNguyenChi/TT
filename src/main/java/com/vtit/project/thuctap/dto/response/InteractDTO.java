package com.vtit.project.thuctap.dto.response;

import com.vtit.project.thuctap.constant.enums.EStatusInteract;
import com.vtit.project.thuctap.entity.Post;
import com.vtit.project.thuctap.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class InteractDTO {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private EStatusInteract status;
    private Long userId;
    private Long postId;
}
