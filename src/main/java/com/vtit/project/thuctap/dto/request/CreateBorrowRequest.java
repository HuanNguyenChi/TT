package com.vtit.project.thuctap.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data

public class CreateBorrowRequest {
    private Long userId;
    private Integer returnDate;
    private List<CreateBorrowItemRequest> items;
}
