package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.dto.request.CreateBorrowRequest;
import com.vtit.project.thuctap.dto.request.UpdateBorrowRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.BorrowDTO;
import com.vtit.project.thuctap.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/library/borrow")
public class BorrowController {
    private final BorrowService borrowService;

    @GetMapping("")
    public ApiResponse<Page<?>> findAllBook(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                            @RequestParam(value = "userId") Long userId){
        return ApiResponse.<Page<?>>builder()
                .result(borrowService.findAllByUserId(userId, pageable))
                .build();
    }

    @GetMapping("/detail")
    public ApiResponse<?> detail(@RequestParam(value = "id") Long id){
        return ApiResponse.<BorrowDTO>builder()
                .result(borrowService.findById(id))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<?> create(@RequestBody CreateBorrowRequest request){
        return ApiResponse.<BorrowDTO>builder()
                .result(borrowService.save(request))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<?> update(@RequestBody UpdateBorrowRequest request){
        return ApiResponse.<BorrowDTO>builder()
                .result(borrowService.update(request))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> delete(@RequestParam Long id){
        borrowService.delete(id);
        return ApiResponse.<String>builder()
                .result("Borrow has been delete")
                .build();
    }

}
