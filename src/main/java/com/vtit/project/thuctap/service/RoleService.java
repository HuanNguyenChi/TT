package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateRoleRequest;
import com.vtit.project.thuctap.dto.request.ListRoleRequest;
import com.vtit.project.thuctap.dto.request.UpdateRoleRequest;
import com.vtit.project.thuctap.dto.response.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    RoleDTO findByName(String roleUser);
    RoleDTO findByCode(String roleCode);
    RoleDTO findById(long id);
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(UpdateRoleRequest request);
    void deleteRole(List<Long> ids);

    Page<RoleDTO> findRoleFilter(ListRoleRequest request, Pageable pageable);
}
