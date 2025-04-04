package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateUserRequest;
import com.vtit.project.thuctap.dto.request.ListUserRequest;
import com.vtit.project.thuctap.dto.request.UpdateUserRequest;
import com.vtit.project.thuctap.dto.response.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserDTO> findUserFilter(ListUserRequest request, Pageable pageable);
    UserDTO findDetailUserById(Long userId);
    void deleteUserById(List<Long> ids);
    UserDTO createUser(CreateUserRequest createUserRequest);
    UserDTO updateUser(UpdateUserRequest updateUserRequest);
}
