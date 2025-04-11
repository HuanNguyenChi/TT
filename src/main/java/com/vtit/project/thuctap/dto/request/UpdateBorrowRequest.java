package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateBorrowRequest {
    private Long borrowId;
    private Long userId;
    private int returnDate;
    private List<BorrowItemRequest> items;
}
