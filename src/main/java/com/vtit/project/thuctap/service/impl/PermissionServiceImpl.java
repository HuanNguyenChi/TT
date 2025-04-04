package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.dto.request.ListPermissionRequest;
import com.vtit.project.thuctap.dto.response.PermissionDTO;
import com.vtit.project.thuctap.entity.Permission;
import com.vtit.project.thuctap.entity.Role;
import com.vtit.project.thuctap.repository.PermissionRepository;
import com.vtit.project.thuctap.repository.RoleRepository;
import com.vtit.project.thuctap.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;


    @Override
    public Page<PermissionDTO> findPermissionFilter(ListPermissionRequest request, Pageable pageable) {
        return permissionRepository.findPermissionFilter(request, pageable);
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission permission = modelMapper.map(permissionDTO, Permission.class);
        Permission savedPermission = permissionRepository.save(permission);

        List<Role> roles = roleRepository.findAllById(permissionDTO.getIdsRole());
        roles.forEach(role -> role.getPermissionList().add(savedPermission));
        roleRepository.saveAll(roles);

        return modelMapper.map(savedPermission, PermissionDTO.class);
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permissionDTO) {
        Permission permission = modelMapper.map(permissionDTO, Permission.class);
        Permission savedPermission = permissionRepository.save(permission);
        return modelMapper.map(savedPermission, PermissionDTO.class);
    }

    @Override
    public void deletePermission(List<Long> ids) {
        permissionRepository.deleteAllById(ids);
    }

    @Override
    public PermissionDTO findPermissionById(Long id) {
        return modelMapper.map(permissionRepository.findById(id).get(), PermissionDTO.class);
    }

    @Override
    public List<PermissionDTO> findPermissionByRoleCode(String code) {
        List<Permission> permissions = permissionRepository.getPermissionByRoleCode(code);
        return permissions.stream()
                .map(permission -> modelMapper.map(permission, PermissionDTO.class))
                .collect(Collectors.toList());
    }
}
