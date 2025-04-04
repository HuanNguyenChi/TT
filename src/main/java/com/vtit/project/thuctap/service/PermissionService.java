package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.ListPermissionRequest;
import com.vtit.project.thuctap.dto.response.PermissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService {
    Page<PermissionDTO> findPermissionFilter(ListPermissionRequest request, Pageable pageable);
    PermissionDTO createPermission(PermissionDTO permission);
    PermissionDTO updatePermission(PermissionDTO permission);
    void deletePermission(List<Long> ids);
    PermissionDTO findPermissionById(Long id);
    List<PermissionDTO> findPermissionByRoleCode(String code);
}
