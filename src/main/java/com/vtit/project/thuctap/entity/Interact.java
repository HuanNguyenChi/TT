package com.vtit.project.thuctap.entity;

import com.vtit.project.thuctap.constant.enums.EStatusInteract;
import com.vtit.project.thuctap.utlis.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "interact")
@AllArgsConstructor
@NoArgsConstructor
public class Interact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @Enumerated(EnumType.STRING)
    private EStatusInteract status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post postId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = TimeUtil.getCurrentTimestamp();
        this.updatedAt = null;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = TimeUtil.getCurrentTimestamp();
    }

}
