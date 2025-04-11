package com.vtit.project.thuctap.dto.request;

import com.vtit.project.thuctap.constant.enums.EStatusBorrow;
import lombok.Data;

@Data
public class BorrowItemRequest {
    private Long bookId;
    private Integer quantity;
    private EStatusBorrow status;
}
