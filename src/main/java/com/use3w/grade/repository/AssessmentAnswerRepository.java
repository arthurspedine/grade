package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, UUID> {
    Set<AssessmentAnswer> findAllByAssessmentStudentId(UUID id);
}
