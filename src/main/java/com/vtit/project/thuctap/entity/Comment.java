package com.vtit.project.thuctap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vtit.project.thuctap.utlis.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @Column
    private Boolean isActive;
    @Column
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userId;
    @PrePersist
    protected void onCreate() {
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = TimeUtil.getCurrentTimestamp();
        this.updatedAt = null;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = TimeUtil.getCurrentTimestamp();
    }
}
