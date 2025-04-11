package com.vtit.project.thuctap.entity;

import com.vtit.project.thuctap.utlis.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;

    @Column
    private Timestamp publishedAt;

    @Column
    private Boolean isDeleted;
    @Column
    private Boolean isActive;

    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @OneToMany(mappedBy = "postId",fetch = FetchType.LAZY)
    private List<Comment> commentList;

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
