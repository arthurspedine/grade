package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentCategoryRepository extends JpaRepository<AssessmentCategory, UUID> {
}
