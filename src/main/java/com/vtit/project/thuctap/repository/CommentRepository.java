package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.entity.Comment;
import com.vtit.project.thuctap.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
        SELECT c FROM Comment c
        WHERE c.postId.id = :postId
""")
    Page<Comment> findByPost(@Param("postId") Long postId, Pageable pageable);
}
