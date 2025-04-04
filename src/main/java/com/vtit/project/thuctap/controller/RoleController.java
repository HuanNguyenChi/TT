package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.request.ListRoleRequest;
import com.vtit.project.thuctap.dto.request.UpdateRoleRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.ResponseMessage;
import com.vtit.project.thuctap.dto.response.RoleDTO;
import com.vtit.project.thuctap.entity.Role;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ApiResponse<?> createRole(@RequestBody RoleDTO roleDTO) {
        return ApiResponse.<RoleDTO>builder()
                .result(roleService.createRole(roleDTO))
                .build();
    }
    @PutMapping("/update")
    public ApiResponse<?> updateRole(@RequestBody UpdateRoleRequest request) {
        return ApiResponse.<RoleDTO>builder()
                .result(roleService.updateRole(request))
                .build();

    }
    @GetMapping("/detail")
    public ApiResponse<?> detailRole(@RequestParam("id") Long id) {
        return ApiResponse.<RoleDTO>builder()
                .result(roleService.findById(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<?> findRoleFilter(@RequestBody ListRoleRequest request,
                                         @PageableDefault(page = 0, size = 10, sort = "code", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.<Page<?>>builder()
                .result(roleService.findRoleFilter(request, pageable))
                .build();
    }
    @DeleteMapping("/delete")
    public ApiResponse<?> deleteRole(@RequestParam("ids") List<Long> ids) {
        roleService.deleteRole(ids);
        return ApiResponse.<String>builder()
                .result("Role has been deleted")
                .build();
    }

}
