package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.request.ListPermissionRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.PermissionDTO;
import com.vtit.project.thuctap.dto.response.ResponseMessage;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping()
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> getPermissionFilter(@RequestBody ListPermissionRequest request,
                                              @PageableDefault(page = 0, size = 10, sort = "code", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.<Page<?>>builder()
                .result(permissionService.findPermissionFilter(request, pageable))
                .build();
    }

    @GetMapping("/detail")
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> getPermissionDetail(@RequestParam("id") Long id) {
        return ApiResponse.<PermissionDTO>builder()
                .result(permissionService.findPermissionById(id))
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return ApiResponse.<PermissionDTO>builder()
                .result(permissionService.createPermission(permissionDTO))
                .build();
    }

    @PutMapping("/update")
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> updatePermission(@RequestBody PermissionDTO permissionDTO) {
        return ApiResponse.<PermissionDTO>builder()
                .result(permissionService.updatePermission(permissionDTO))
                .build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> deletePermission(@RequestParam("ids") List<Long> ids) {
       permissionService.deletePermission(ids);
       return ApiResponse.<String>builder()
               .result("Permission has been deleted")
               .build();
    }

    @GetMapping("/find-by-code")
    @PreAuthorize(" hasRole('ADMIN')")
    public ApiResponse<?> findPermissionByCode(@RequestBody String code) {
        return ApiResponse.<List<PermissionDTO>>builder()
                .result(permissionService.findPermissionByRoleCode(code))
                .build();

    }


}
