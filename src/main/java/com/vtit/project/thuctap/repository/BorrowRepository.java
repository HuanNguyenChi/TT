package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.entity.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow,Long> {

    @EntityGraph(attributePaths = "borrowItemList")
    @Query("""
    SELECT br
    FROM Borrow br
    LEFT JOIN br.borrowItemList bi
    WHERE br.userId.id = :userId
    ORDER BY
        CASE WHEN bi.status = 'BORROWED' THEN 0 ELSE 1 END,
        br.borrowAt DESC
    """)
    Page<Borrow> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @EntityGraph(attributePaths = "borrowItemList")
    @Query("SELECT b FROM Borrow b WHERE b.id = :borrowId")
    Optional<Borrow> findByIdWithItems(@Param("borrowId") Long borrowId);

    @Query("""
    SELECT b FROM Borrow b
    JOIN  b.borrowItemList bi
    JOIN  b.userId u
    WHERE bi.status = 'BORROWED' AND u.isActive = true
        AND b.expireAt <= :now OR b.expireAt <= :plus2Day
        AND u.isDeleted = false
    """)
    List<Borrow> findBorrowsDueSoon(@Param("now") LocalDateTime now,@Param("plus2Day") LocalDateTime plus2Day);
}
