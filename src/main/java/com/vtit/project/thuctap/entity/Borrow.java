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
@Table(name = "borrow")
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Timestamp borrowAt;
    @Column
    private Timestamp expireAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @OneToMany(mappedBy = "borrowId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BorrowItem> borrowItemList;

    @PrePersist
    protected void onCreate() {
        this.borrowAt = TimeUtil.getCurrentTimestamp();
        if (this.expireAt == null) {
            this.expireAt = TimeUtil.getExpireTimestamp(7); // Mặc định +7 ngày
        }
    }


}
