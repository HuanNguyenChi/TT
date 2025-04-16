package com.vtit.project.thuctap.entity;

import com.vtit.project.thuctap.utlis.TimeUtil;
import jakarta.persistence.*;
import lombok.*;


import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String description;
    @Column
    private Integer quantity;
    @Column
    private Integer publishedAt;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp updatedAt;
    @Column
    private Boolean isActive;
    @Column
    private Boolean isDeleted;
    @Column
    private Integer totalQuantity;
    @Column
    private Integer remainQuantity;

    @Column
    private int createdBy;
    @Column
    private int updatedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList;

    @OneToMany(mappedBy = "bookId", fetch = FetchType.LAZY)
    private List<BorrowItem> borrowItemList;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", publishedAt=" + publishedAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                ", isDeleted=" + isDeleted +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                '}';
    }

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
