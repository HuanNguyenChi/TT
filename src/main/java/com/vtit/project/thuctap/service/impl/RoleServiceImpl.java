package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.ListRoleRequest;
import com.vtit.project.thuctap.dto.request.UpdateRoleRequest;
import com.vtit.project.thuctap.dto.response.RoleDTO;
import com.vtit.project.thuctap.entity.Role;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.RoleRepository;
import com.vtit.project.thuctap.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleDTO findByName(String roleUser) {
        return modelMapper.map(roleRepository.findByName(roleUser), RoleDTO.class);
    }

    @Override
    public RoleDTO findByCode(String roleCode) {
        return modelMapper.map(roleRepository.findByCode(roleCode), RoleDTO.class);
    }

    @Override
    public RoleDTO findById(long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.ROLE));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (!roleRepository.existsByCode(roleDTO.getCode())){
            throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.ROLE);
        }
        Role role = modelMapper.map(roleDTO, Role.class);
        return modelMapper.map(roleRepository.save(role), RoleDTO.class);
    }

    @Override
    public RoleDTO updateRole(UpdateRoleRequest request) {
        Role role = roleRepository.findById(request.getId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.EXISTED, ResponseObject.ROLE));
        if (!role.getCode().equals(request.getCode())){
            if(roleRepository.existsByCode(request.getCode())){
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.CODE);
            }
        }
        Role roleMap = modelMapper.map(request, Role.class);
        return modelMapper.map(roleRepository.save(roleMap), RoleDTO.class);
    }

    @Override
    public void deleteRole(List<Long> ids) {
        roleRepository.deleteAllById(ids);
    }

    @Override
    public Page<RoleDTO> findRoleFilter(ListRoleRequest request, Pageable pageable) {

        Page<RoleDTO> roles = roleRepository.findRoleFilter(request, pageable);

        if (roles.isEmpty()){
            throw new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.ROLE);
        }
        return roles;
    }
}
