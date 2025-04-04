package com.vtit.project.thuctap.dto.response;

import lombok.Getter;

import java.util.List;
@Getter
public class ImportExcelResponse {
    private List<BookDTO> importedBooks;
    private List<String> errors;

    public ImportExcelResponse(List<BookDTO> importedBooks, List<String> errors) {
        this.importedBooks = importedBooks;
        this.errors = errors;
    }

    // Getters
}
