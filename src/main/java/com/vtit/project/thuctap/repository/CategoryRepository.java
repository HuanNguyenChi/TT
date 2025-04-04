package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.dto.request.ListCategoryRequest;
import com.vtit.project.thuctap.dto.response.CategoryDTO;
import com.vtit.project.thuctap.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.vtit.project.thuctap.dto.response.CategoryDTO(c.code, c.description, c.name) FROM Category  c " +
            " WHERE (:#{#request.code} IS NULL OR c.code LIKE CONCAT('%', :#{#request.code}, '%') )" +
            "AND (:#{#request.description} IS NULL OR c.description LIKE CONCAT('%', :#{#request.description}, '%') )" +
            "AND (:#{#request.name} IS NULL OR c.name LIKE CONCAT('%', :#{#request.name}, '%') )")
    Page<CategoryDTO> findCategoryFilter(@Param("request") ListCategoryRequest request, Pageable pageable);

    boolean existsByCode(String code);
}
