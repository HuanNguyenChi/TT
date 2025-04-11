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
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private String fullname;
    @Column(unique = true)
    private String email;
    @Column
    private String phone;
    @Column
    private String address;
    @Column
    private Timestamp dob;
    @Column(unique = true)
    private String identityNumber;
    @Column(updatable = false)
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @Column
    private Boolean isActive;
    @Column
    private Boolean isDeleted;

    @ManyToMany(mappedBy = "userList", fetch = FetchType.LAZY)
    private List<Role> roleList;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Borrow> borrowList;

    @OneToMany(mappedBy = "userId")
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
