package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.dto.request.ListRoleRequest;
import com.vtit.project.thuctap.dto.response.RoleDTO;
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
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String roleUser);
    @Query("SELECT r FROM Role r WHERE r.code IN :roleCodes")
    List<Role> findByCode(@Param("roleCodes") List<String> roleCodes);

    @Query("SELECT new com.vtit.project.thuctap.dto.response.RoleDTO(r.code, r.description, r.name) FROM Role r " +
            " WHERE (:#{#request.code} IS NULL OR r.code LIKE CONCAT('%', :#{#request.code}, '%') )" +
            "AND (:#{#request.description} IS NULL OR r.description LIKE CONCAT('%', :#{#request.description}, '%') )" +
            "AND (:#{#request.name} IS NULL OR r.name LIKE CONCAT('%', :#{#request.name}, '%') )")
    Page<RoleDTO> findRoleFilter(@Param("request") ListRoleRequest request, Pageable pageable);


    boolean existsByCode(String code);
}
