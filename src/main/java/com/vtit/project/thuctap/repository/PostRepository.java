package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
        SELECT p
        FROM Post p
        WHERE p.createdBy.id = :userId
""")
    Page<Post> findAllByUserId(Pageable pageable,@Param("userId") Long userId);
    @Query("""
    SELECT p FROM Post p
        WHERE p.id IN (
            SELECT i.postId.id FROM Interact i
            WHERE i.status = 'LIKE'
            GROUP BY i.postId.id
            ORDER BY COUNT(i) DESC
        )
""")
    List<Post> findAllByReact(Pageable pageable);
}
