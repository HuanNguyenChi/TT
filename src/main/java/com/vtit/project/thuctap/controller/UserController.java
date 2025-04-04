package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.dto.request.CreateUserRequest;
import com.vtit.project.thuctap.dto.request.ListUserRequest;
import com.vtit.project.thuctap.dto.request.UpdateUserRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.UserDTO;
import com.vtit.project.thuctap.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping(value = "")
    public ApiResponse<Page<?>>  findUserFilter(@RequestBody ListUserRequest request,
                                                @PageableDefault(page = 0, size = 10, sort = "username", direction = Sort.Direction.ASC)Pageable pageable) {
        return ApiResponse.<Page<?>>builder()
                .result(userService.findUserFilter(request,pageable))
                .build();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createUser(@RequestBody CreateUserRequest request){
        return ApiResponse.<UserDTO>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping(value = "/detail")
    public ApiResponse<?> findDetailUserById(@RequestParam("id") Long id){
        return ApiResponse.<UserDTO>builder()
                .result(userService.findDetailUserById(id))
                .build();
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateUser(@RequestBody UpdateUserRequest request){
        return ApiResponse.<UserDTO>builder()
                .result(userService.updateUser(request))
                .build();
    }

    @DeleteMapping(value = "/delete")
    public ApiResponse<?> deleteUserById(@RequestParam("ids") List<Long> ids){
        userService.deleteUserById(ids);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }


}
