package com.vtit.project.thuctap.dto.response;

import com.vtit.project.thuctap.constant.enums.EStatusBorrow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowItemDTO {
    private EStatusBorrow status;
    private Integer quantity;
    private Timestamp returnAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private BookDTO bookDTO;
    private Integer overdueDays;


}
