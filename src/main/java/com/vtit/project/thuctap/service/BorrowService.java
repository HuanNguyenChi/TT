package com.vtit.project.thuctap.service;

import com.vtit.project.thuctap.dto.request.CreateBorrowRequest;
import com.vtit.project.thuctap.dto.request.UpdateBorrowRequest;
import com.vtit.project.thuctap.dto.response.BorrowDTO;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  BorrowService {
    Page<BorrowDTO> findAllByUserId(Long userId, Pageable pageable);
    BorrowDTO findById( Long id);
    BorrowDTO save(CreateBorrowRequest request);
    BorrowDTO update(UpdateBorrowRequest request);
    void delete(Long borrowId);
}
