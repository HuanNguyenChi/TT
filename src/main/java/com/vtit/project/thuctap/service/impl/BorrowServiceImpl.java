package com.vtit.project.thuctap.service.impl;

import com.vtit.project.thuctap.constant.enums.EStatusBorrow;
import com.vtit.project.thuctap.constant.enums.ResponseCode;
import com.vtit.project.thuctap.constant.enums.ResponseObject;
import com.vtit.project.thuctap.dto.request.CreateBorrowRequest;
import com.vtit.project.thuctap.dto.request.UpdateBorrowRequest;
import com.vtit.project.thuctap.dto.response.BookDTO;
import com.vtit.project.thuctap.dto.response.BorrowDTO;
import com.vtit.project.thuctap.dto.response.BorrowItemDTO;
import com.vtit.project.thuctap.entity.Book;
import com.vtit.project.thuctap.entity.Borrow;
import com.vtit.project.thuctap.entity.BorrowItem;
import com.vtit.project.thuctap.entity.User;
import com.vtit.project.thuctap.exception.ThucTapException;
import com.vtit.project.thuctap.repository.BookRepository;
import com.vtit.project.thuctap.repository.BorrowRepository;
import com.vtit.project.thuctap.repository.UserRepository;
import com.vtit.project.thuctap.service.BorrowService;
import com.vtit.project.thuctap.utlis.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<BorrowDTO> findAllByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER);
        }

        Page<Borrow> borrowPage = borrowRepository.findAllByUserId(userId, pageable);

        List<BorrowDTO> dtos = borrowPage.getContent().stream()
                .map(borrow -> {
                    BorrowDTO dto = modelMapper.map(borrow, BorrowDTO.class);

                    dto.setBorrowItemDTOList(borrow.getBorrowItemList().stream()
                            .map(item -> {
                                BorrowItemDTO itemDto = modelMapper.map(item, BorrowItemDTO.class);
                                itemDto.setOverdueDays(calculateItemOverdueDays(item, borrow.getExpireAt()));
                                return itemDto;
                            })
                            .toList());

                    return dto;
                })
                .toList();

        return new PageImpl<>(dtos, pageable, borrowPage.getTotalElements());
    }

    private Integer calculateItemOverdueDays(BorrowItem item, Timestamp borrowExpireAt) {
        if (item.getStatus() != EStatusBorrow.RETURNED && borrowExpireAt != null) {
            long days = ChronoUnit.DAYS.between(
                    borrowExpireAt.toInstant(),
                    LocalDate.now()
            );
            return days > 0 ? (int) days : null;
        }
        return null;
    }


    @Override
    public BorrowDTO findById(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BORROW));
        BorrowDTO dto = modelMapper.map(borrow, BorrowDTO.class);

        dto.setBorrowItemDTOList(borrow.getBorrowItemList().stream()
                .map(item -> {
                    BorrowItemDTO itemDto = modelMapper.map(item, BorrowItemDTO.class);

                    // Map thông tin sách
                    itemDto.setBookDTO(modelMapper.map(item.getBookId(), BookDTO.class));

                    return itemDto;
                })
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    @Transactional
    public BorrowDTO save(CreateBorrowRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        Borrow borrow = new Borrow();
        borrow.setBorrowAt(TimeUtil.getCurrentTimestamp());
        borrow.setExpireAt(TimeUtil.getExpireTimestamp(request.getReturnDate()));
        borrow.setUserId(user);

        List<BorrowItem> borrowItems = request.getItems().stream()
                .map(itemRequest -> {
                    Book book = bookRepository.findById(itemRequest.getBookId())
                            .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BOOK));

                    if (itemRequest.getQuantity() <= 0) {
                        throw new ThucTapException(ResponseCode.INVALID_QUANTITY, ResponseObject.BOOK);
                    }
                    if (book.getRemainQuantity() < itemRequest.getQuantity()) {
                        throw new ThucTapException(ResponseCode.OUT_OF_STOCK, ResponseObject.BOOK);
                    }

                    BorrowItem borrowItem = new BorrowItem();
                    borrowItem.setBookId(book);
                    borrowItem.setQuantity(itemRequest.getQuantity());

                    // Xử lý status an toàn
                    EStatusBorrow status;
                    try {
                        status = itemRequest.getStatus() != null
                                ? EStatusBorrow.valueOf(itemRequest.getStatus().name())
                                : EStatusBorrow.BORROWED;
                    } catch (IllegalArgumentException e) {
                        status = EStatusBorrow.BORROWED; // Fallback nếu status không hợp lệ
                    }
                    borrowItem.setStatus(status);

                    borrowItem.setCreatedAt(TimeUtil.getCurrentTimestamp());

                    if (status == EStatusBorrow.BORROWED) {
                        book.setRemainQuantity(book.getRemainQuantity() - itemRequest.getQuantity());
                        bookRepository.save(book);
                    }

                    return borrowItem;
                })
                .collect(Collectors.toList());

        borrow.setBorrowItemList(borrowItems);
        Borrow savedBorrow = borrowRepository.save(borrow);

        // Set borrowId cho từng item (quan trọng với quan hệ hai chiều)
        borrowItems.forEach(item -> item.setBorrowId(savedBorrow));

        // Map sang DTO với danh sách items
        BorrowDTO dto = modelMapper.map(savedBorrow, BorrowDTO.class);

        if (savedBorrow.getBorrowItemList() != null) {
            dto.setBorrowItemDTOList(savedBorrow.getBorrowItemList().stream()
                    .map(item -> {
                        BorrowItemDTO itemDto = modelMapper.map(item, BorrowItemDTO.class);

                        // Map thông tin sách
                        itemDto.setBookDTO(modelMapper.map(item.getBookId(), BookDTO.class));

                        return itemDto;
                    })
                    .collect(Collectors.toList()));
        }

        return modelMapper.map(dto, BorrowDTO.class);
    }

    @Override
    @Transactional
    public BorrowDTO update(UpdateBorrowRequest request) {
        Borrow borrow = borrowRepository.findById(request.getBorrowId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BORROW));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.USER));

        borrow.setUserId(user);
        borrow.setExpireAt(TimeUtil.getExpireTimestamp(request.getReturnDate()));

        List<BorrowItem> newItems = request.getItems().stream()
                .map(itemRequest -> {
                    Book book = bookRepository.findById(itemRequest.getBookId())
                            .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BOOK));

                    if (itemRequest.getQuantity() <= 0) {
                        throw new ThucTapException(ResponseCode.INVALID_QUANTITY, ResponseObject.BOOK);
                    }

                    BorrowItem item = borrow.getBorrowItemList().stream()
                            .filter(i -> i.getBookId().equals(book))
                            .findFirst()
                            .orElse(new BorrowItem());

                    item.setBookId(book);
                    item.setQuantity(itemRequest.getQuantity());
                    if (itemRequest.getStatus() == EStatusBorrow.RETURNED) {
                        Book book1 = item.getBookId();
                        book1.setRemainQuantity(book.getRemainQuantity() + item.getQuantity());
                        bookRepository.save(book);
                        item.setStatus(EStatusBorrow.RETURNED);
                        item.setReturnAt(TimeUtil.getCurrentTimestamp());
                    } else if (item.getStatus() == EStatusBorrow.RETURNED &&
                            itemRequest.getStatus() != EStatusBorrow.RETURNED) {
                        Book book1 = item.getBookId();
                        if (book1.getRemainQuantity() < item.getQuantity()) {
                            throw new ThucTapException(ResponseCode.OUT_OF_STOCK, ResponseObject.BOOK);
                        }
                        book.setRemainQuantity(book.getRemainQuantity() - item.getQuantity());
                        bookRepository.save(book);
                        item.setStatus(EStatusBorrow.BORROWED);
                        item.setReturnAt(TimeUtil.getCurrentTimestamp());
                    }

                    if (item.getId() == null) {
                        item.setCreatedAt(TimeUtil.getCurrentTimestamp());
                    } else {
                        item.setUpdatedAt(TimeUtil.getCurrentTimestamp());
                    }

                    return item;
                })
                .collect(Collectors.toList());

        borrow.getBorrowItemList().removeIf(item ->
                !newItems.stream()
                        .anyMatch(newItem -> newItem.getBookId().equals(item.getBookId()))
        );

        borrow.getBorrowItemList().addAll(newItems);
        Borrow savedBorrow = borrowRepository.save(borrow);

        newItems.forEach(item -> item.setBorrowId(savedBorrow));

        BorrowDTO dto = modelMapper.map(savedBorrow, BorrowDTO.class);

        if (savedBorrow.getBorrowItemList() != null) {
            dto.setBorrowItemDTOList(savedBorrow.getBorrowItemList().stream()
                    .map(item -> {
                        BorrowItemDTO itemDto = modelMapper.map(item, BorrowItemDTO.class);

                        itemDto.setBookDTO(modelMapper.map(item.getBookId(), BookDTO.class));

                        return itemDto;
                    })
                    .collect(Collectors.toList()));
        }

        return modelMapper.map(dto, BorrowDTO.class);
    }

    @Override
    @Transactional
    public void delete(Long borrowId) {
        // 1. Tìm borrow cần xóa với fetch cả danh sách items
        Borrow borrow = borrowRepository.findByIdWithItems(borrowId)
                .orElseThrow(() -> new ThucTapException(ResponseCode.NOT_EXISTED, ResponseObject.BORROW));

        // 2. Kiểm tra điều kiện trước khi xóa
        if (hasReturnedItems(borrow)) {
            throw new ThucTapException(ResponseCode.CANNOT_DELETE_RETURNED_BORROW,
                    ResponseObject.BORROW);
        }

        // 3. Cập nhật số lượng sách trong kho (nếu cần)
        restoreBookQuantities(borrow);

        // 4. Thực hiện xóa
        borrowRepository.delete(borrow);

    }

    private boolean hasReturnedItems(Borrow borrow) {
        return borrow.getBorrowItemList().stream()
                .anyMatch(item -> item.getStatus() == EStatusBorrow.RETURNED);
    }

    private void restoreBookQuantities(Borrow borrow) {
        borrow.getBorrowItemList().forEach(item -> {
            // Chỉ hoàn trả số lượng nếu sách chưa được trả
            if (item.getStatus() != EStatusBorrow.RETURNED) {
                Book book = item.getBookId();
                book.setRemainQuantity(book.getRemainQuantity() + item.getQuantity());
                bookRepository.save(book);
            }
        });
    }
}
