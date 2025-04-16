package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.FieldUpdate;
import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreateBookRequest;
import com.vtit.project.thuctap.dto.request.ListBookRequest;
import com.vtit.project.thuctap.dto.request.UpdateBookRequest;
import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.ImportExcelResponse;
import com.vtit.project.thuctap.entity.Book;
import com.vtit.project.thuctap.entity.Category;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.BookRepository;
import com.vtit.project.thuctap.repository.CategoryRepository;
import com.vtit.project.thuctap.service.BookService;
import com.vtit.project.thuctap.service.CategoryService;
import com.vtit.project.thuctap.utlis.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<BookDTO> findBookFilter(ListBookRequest request,Pageable pageable) {
        return bookRepository.findBookFilter(request, pageable);

    }

    @Override
    public BookDTO getBookDetailById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BOOK));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookDTO createBook(CreateBookRequest request) {
        Book book = modelMapper.map(request, Book.class);
        if(!bookRepository.existsByCode(book.getCode())) {
            throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.CODE);
        }
        List<Category> categoryList = categoryRepository.findAllById(request.getCategoryList());

        book.setCategoryList(categoryList);
        Book savedBook = bookRepository.save(book);

        categoryList.forEach(category -> category.getBookList().add(savedBook));
        categoryRepository.saveAll(categoryList);
        return modelMapper.map(savedBook, BookDTO.class);
    }
    @Override
    public BookDTO updateBook(UpdateBookRequest request) {
        // Tìm kiếm sách theo ID
        Book book = bookRepository.findById(request.getId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BOOK));

        // Kiểm tra và cập nhật các trường nếu cần thiết
        if (!book.getCode().equals(request.getCode())) {
            if (bookRepository.existsByCode(request.getCode())) {
                throw new ThucTapException(ResponseCode.EXISTED, ResponseObject.CODE);
            }
            book.setCode(request.getCode());
        }

        // Sử dụng Stream để kiểm tra và cập nhật các trường còn lại
        List.of(
                new FieldUpdate<>(book::getCode, book::setCode, request.getCode()),  // String
                new FieldUpdate<>(book::getTitle, book::setTitle, request.getTitle()),  // String
                new FieldUpdate<>(book::getAuthor, book::setAuthor, request.getAuthor()),  // String
                new FieldUpdate<>(book::getDescription, book::setDescription, request.getDescription()),  // String
                new FieldUpdate<>(book::getQuantity, book::setQuantity, request.getQuantity()),  // Integer
                new FieldUpdate<>(book::getIsActive, book::setIsActive, request.isActive())  // Boolean
        ).forEach(FieldUpdate::updateIfNeeded);

        // Xử lý danh sách categoryList với Stream
        List<Category> newCategories = categoryRepository.findAllById(request.getCategoryList());
        Set<Category> existingCategories = new HashSet<>(book.getCategoryList());
        existingCategories.addAll(newCategories);  // Thêm tất cả category mới vào set

        book.setCategoryList(new ArrayList<>(existingCategories));

        // Lưu sách đã được cập nhật
        Book updatedBook = bookRepository.save(book);

        // Chuyển đổi thành BookDTO để trả về cho client
        return modelMapper.map(updatedBook, BookDTO.class);
    }

    @Override
    public void deleteBookById(List<Long> ids) {
        bookRepository.deleteAllById(ids);
    }

    @Override
    public ByteArrayOutputStream exportBook() {
        List<Book> books = bookRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Books");

            // Tạo header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID","Code", "Title", "Author","Quantity", "Published Year", "Active"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (Book book : books) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(book.getId());
                row.createCell(1).setCellValue(book.getCode());
                row.createCell(2).setCellValue(book.getTitle());
                row.createCell(3).setCellValue(book.getAuthor());
                row.createCell(4).setCellValue(book.getQuantity());
                row.createCell(5).setCellValue(book.getPublishedAt());
                row.createCell(6).setCellValue(book.getIsActive());

            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);



            // Lưu file vào ổ đĩa D:/vtit
            saveToLocalDisk(outputStream.toByteArray());
            return outputStream;
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel", e);
        }
    }
    private void saveToLocalDisk(byte[] data) {
        String directoryPath = "D:/vtit/excel";
        String filePath = directoryPath + "/books_export_" + System.currentTimeMillis() + ".xlsx";

        try {
            // Tạo thư mục nếu chưa tồn tại
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Ghi file
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                fileOutputStream.write(data);
                System.out.println("File saved to: " + filePath);
            }
        } catch (Exception e) {
            System.err.println("Error saving file to local disk: " + e.getMessage());
        }
    }




    public ResponseEntity<?> processExcelImport(MultipartFile file) {
        if (!isExcelFile(file)) {
            return ResponseEntity.badRequest().body("Only Excel files are allowed");
        }

        ImportExcelResponse result = importBooks(file);

        if (!result.getErrors().isEmpty()) {
            Map<String, Object> response = createErrorResponse(result);
            saveValidBooks(result.getImportedBooks());
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }

        saveValidBooks(result.getImportedBooks());
        return createSuccessResponse(result);
    }

    private boolean isExcelFile(MultipartFile file) {
        return file.getContentType() != null &&
                file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private ImportExcelResponse importBooks(MultipartFile file) {
        List<BookDTO> importedBookDTOs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> seenCodes = new HashSet<>(); // Theo dõi mã sách trong file Excel

        // Lấy tất cả mã sách từ DB
        List<Book> existingBooks = bookRepository.findAll();
        Set<String> existingCodes = existingBooks.stream()
                .map(Book::getCode)
                .collect(Collectors.toSet());

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Bỏ qua header

            int rowNum = 1;
            while (rows.hasNext()) {
                rowNum++;
                Row currentRow = rows.next();
                processRow(currentRow, rowNum, importedBookDTOs, errors, seenCodes, existingCodes);
            }

        } catch (IOException e) {
            errors.add("File Error: Error reading Excel file: " + e.getMessage());
        }

        return new ImportExcelResponse(importedBookDTOs, errors);
    }

    private void processRow(Row currentRow, int rowNum, List<BookDTO> importedBooks,
                            List<String> errors, Set<String> seenCodes, Set<String> existingCodes) {
        try {
            BookDTO book = new BookDTO();
            book.setCode(getStringValue(currentRow.getCell(0)));
            book.setTitle(getStringValue(currentRow.getCell(1)));
            book.setAuthor(getStringValue(currentRow.getCell(2)));
            book.setQuantity(Integer.parseInt(getStringValue(currentRow.getCell(3))));
            book.setPublishedAt(Integer.parseInt(getStringValue(currentRow.getCell(4))));
            book.setIsActive(Boolean.valueOf(getStringValue(currentRow.getCell(5))));

            try {
                book.setQuantity((int) currentRow.getCell(3).getNumericCellValue());
            } catch (Exception e) {
                throw new IllegalArgumentException("Quantity must be a number");
            }

            // Kiểm tra mã sách
            if (book.getCode() == null || book.getCode().isEmpty()) {
                throw new IllegalArgumentException("Code is required");
            }

            // Kiểm tra trùng mã trong DB
            if (existingCodes.contains(book.getCode())) {
                throw new IllegalArgumentException("Code already exists in database");
            }

            // Kiểm tra trùng mã trong file Excel
            if (!seenCodes.add(book.getCode())) { // add() trả về false nếu mã đã tồn tại
                throw new IllegalArgumentException("Duplicate code in Excel file");
            }

            if (book.getTitle() == null || book.getTitle().isEmpty()) {
                throw new IllegalArgumentException("Title is required");
            }

            importedBooks.add(book);
        } catch (Exception e) {
            errors.add("Row " + rowNum + ": " + e.getMessage());
        }
    }

    private void saveValidBooks(List<BookDTO> books) {
        if (!books.isEmpty()) {
            List<Book> booksToSave = books.stream()
                    .map(bookDTO -> modelMapper.map(bookDTO, Book.class))
                    .collect(Collectors.toList());
            bookRepository.saveAll(booksToSave);
        }
    }

    private Map<String, Object> createErrorResponse(ImportExcelResponse result) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Import completed with errors");
        response.put("successCount", result.getImportedBooks().size());
        response.put("errorCount", result.getErrors().size());
        response.put("errors", result.getErrors());
        return response;
    }

    private ResponseEntity<?> createSuccessResponse(ImportExcelResponse result) {
        return ResponseEntity.ok().body(Map.of(
                "message", "Import successful",
                "importedCount", result.getImportedBooks().size()
        ));
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
