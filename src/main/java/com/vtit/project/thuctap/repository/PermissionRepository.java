package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.dto.request.ListPermissionRequest;
import com.vtit.project.thuctap.dto.response.PermissionDTO;
import com.vtit.project.thuctap.entity.Permission;
import com.vtit.project.thuctap.entity.Role;
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
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT new com.vtit.project.thuctap.dto.response.PermissionDTO(p.code, p.description, p.name)" +
            " FROM Permission p " +
            " WHERE (:#{#request.code} IS NULL OR p.code LIKE CONCAT('%', :#{#request.code}, '%') )" +
            "AND (:#{#request.description} IS NULL OR p.description LIKE CONCAT('%', :#{#request.description}, '%') )" +
            "AND (:#{#request.name} IS NULL OR p.name LIKE CONCAT('%', :#{#request.name}, '%') )")
    Page<PermissionDTO> findPermissionFilter(@Param("request") ListPermissionRequest request, Pageable pageable);


    @Query("SELECT p FROM Permission p" +
            " LEFT JOIN p.roleList r " +
            " WHERE r.code LIKE CONCAT('%', :code, '%') ")
    List<Permission> getPermissionByRoleCode(@Param("code") String code);

}
