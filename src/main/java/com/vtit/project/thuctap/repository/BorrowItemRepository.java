package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.entity.Borrow;
import com.vtit.project.thuctap.entity.BorrowItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem,Long> {

}
