package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateBookRequest;
import com.vtit.project.thuctap.dto.request.ListBookRequest;
import com.vtit.project.thuctap.dto.request.UpdateBookRequest;
import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.ImportExcelResponse;
import com.vtit.project.thuctap.dto.response.UserDTO;
import com.vtit.project.thuctap.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public interface BookService {
    Page<BookDTO> findBookFilter(ListBookRequest request, Pageable pageable);
    BookDTO getBookDetailById(Long bookId);
    BookDTO createBook(CreateBookRequest request);
    BookDTO updateBook(UpdateBookRequest request);
    void deleteBookById(List<Long> ids);

    ByteArrayOutputStream exportBook();
    ResponseEntity<?> processExcelImport(MultipartFile file);

}
