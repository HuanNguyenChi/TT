package com.vtit.project.thuctap.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDTO {
    private Long id;
    private Timestamp borrowAt;
    private Timestamp expireAt;
    private UserDTO user;
    private List<BorrowItemDTO> borrowItemDTOList;
}
