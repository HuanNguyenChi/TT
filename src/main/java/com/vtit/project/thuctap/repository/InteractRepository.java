package com.vtit.project.thuctap.repository;

import com.vtit.project.thuctap.entity.Interact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteractRepository extends JpaRepository<Interact, Long> {
}
