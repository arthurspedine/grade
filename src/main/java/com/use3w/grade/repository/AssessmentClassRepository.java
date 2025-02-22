package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentClassRepository extends JpaRepository<AssessmentClass, UUID> {
}
