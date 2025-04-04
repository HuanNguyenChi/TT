package com.vtit.project.thuctap.controller;

import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreateBookRequest;
import com.vtit.project.thuctap.dto.request.ListBookRequest;
import com.vtit.project.thuctap.dto.request.UpdateBookRequest;
import com.vtit.project.thuctap.dto.response.ApiResponse;
import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.ImportExcelResponse;
import com.vtit.project.thuctap.dto.response.ResponseMessage;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/library/book",  produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Page<?>> findBookFilter(@RequestBody ListBookRequest request,
                                               @PageableDefault(page = 0, size = 10, sort = "code", direction = Sort.Direction.ASC)Pageable pageable){
        return ApiResponse.<Page<?>>builder()
                .result(bookService.findBookFilter(request, pageable))
                .build();
    }


    @GetMapping("/detail")
    public ApiResponse<?> findDetailBookById(@RequestParam("id") Long id){
        BookDTO book = bookService.getBookDetailById(id);
        return ApiResponse.<BookDTO>builder()
                .result(book)
                .build();
    }

    @PostMapping(value = "/create")

    public ApiResponse<?> createBook(@RequestBody CreateBookRequest request){
        return ApiResponse.<BookDTO>builder()
                .result(bookService.createBook(request))
                .build();

    }

    @PutMapping("/update")
    public ApiResponse<?> updateBook(@RequestBody UpdateBookRequest request){
        return ApiResponse.<BookDTO>builder()
                .result(bookService.updateBook(request))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<?> deleteBook(@RequestParam List<Long> ids){
        bookService.deleteBookById(ids);
        return ApiResponse.<String>builder()
                .result("Book has been deleted")
                .build();
    }


    @GetMapping("/export")
    public ResponseEntity<Resource> exportBooks() {
        ByteArrayOutputStream outputStream = bookService.exportBook();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books_export.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping("/import")
    public ApiResponse<?> importBooks(@RequestBody MultipartFile file) {
        return ApiResponse.builder()
                .result(bookService.processExcelImport(file))
                .build();
    }

}
