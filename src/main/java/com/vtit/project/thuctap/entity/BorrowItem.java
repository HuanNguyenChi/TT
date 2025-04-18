package com.vtit.project.thuctap.entity;

import com.vtit.project.thuctap.constant.enums.EStatusBorrow;
import com.vtit.project.thuctap.utlis.TimeUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "borrow_item")
@AllArgsConstructor
@NoArgsConstructor
public class BorrowItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED'")
    private EStatusBorrow status = EStatusBorrow.BORROWED;
    @Column
    private Integer quantity;
    @Column
    private Timestamp returnAt;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrow_id", nullable = false)
    private Borrow borrowId;

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
