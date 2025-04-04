package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.dto.request.ListUserRequest;
import com.vtit.project.thuctap.dto.response.UserDTO;
import com.vtit.project.thuctap.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.vtit.project.thuctap.dto.response.UserDTO(u.fullname, u.email,u.phone, u.address, u.dob, u.identityNumber, u.createdAt, u.updatedAt, u.isActive, u.isDeleted)" +
            " FROM User u " +
            "WHERE " +
            "( :#{#request.fullname} IS NULL OR LOWER(u.fullname) LIKE LOWER(CONCAT('%', :#{#request.fullname}, '%'))) " +
            "AND ( :#{#request.phone} IS NULL OR u.phone LIKE CONCAT('%', :#{#request.phone}, '%')) " +
            "AND ( :#{#request.identityNumber} IS NULL OR u.identityNumber LIKE CONCAT('%', :#{#request.identityNumber}, '%')) " +
            "AND ( :#{#request.address} IS NULL OR LOWER(u.address) LIKE LOWER(CONCAT('%', :#{#request.address}, '%'))) " +
            "AND ( :#{#request.isActive} IS NULL OR u.isActive = :#{#request.isActive}) " +
            "AND ( :#{#request.isDeleted} IS NULL OR u.isDeleted = :#{#request.isDeleted})")
    Page<UserDTO> findUserFilter(@Param("request") ListUserRequest request, Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByIdentityNumber(String identityNumber);
    boolean existsByUsername(String username);


}
