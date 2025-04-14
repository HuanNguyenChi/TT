package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.dto.request.ListBookRequest;
import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.vtit.project.thuctap.dto.response.BookDTO(b.code, b.title, b.author, b.description, b.quantity,b.publishedAt, b.createdAt, b.updatedAt, b.isActive, b.isDeleted) " +
            "FROM Book b " +
            "LEFT JOIN b.categoryList c  " +
            "WHERE " +
            "(:#{#request.code} IS NULL OR b.code LIKE CONCAT('%', :#{#request.code}, '%')) " +
            "AND (:#{#request.author} IS NULL  OR b.author LIKE CONCAT('%', :#{#request.author}, '%')) " +
            "AND (:#{#request.publishedAt} IS NULL OR b.publishedAt = :#{#request.publishedAt}) " +
            "AND (:#{#request.categoryList} IS NULL OR (c IS NOT NULL AND c.id IN (:#{#request.categoryList})) ) " +
            "AND (:#{#request.isActive} IS NULL OR b.isActive = :#{#request.isActive}) " +
            "AND (:#{#request.isDeleted} IS NULL OR b.isDeleted = :#{#request.isDeleted})")
    Page<BookDTO> findBookFilter(@Param("request") ListBookRequest request, Pageable pageable);

    boolean existsByCode(String code);


    List<Book> findAllByCodeIn(List<String> codes);
    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN b.categoryList c
    WHERE c.id = :categoryId OR :categoryId IS NULL
    """)
    List<Book> findAllByCategory(@Param("categoryId") Long categoryId);
}
