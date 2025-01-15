package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByCreatedBy(String createdBy);
}
