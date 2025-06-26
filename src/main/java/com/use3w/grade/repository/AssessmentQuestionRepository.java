package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, UUID> {
}
