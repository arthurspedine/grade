package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, UUID> {
    @Query(value = """
        select aQ
        from AssessmentQuestion aQ
        where aQ.assessment.id = :assessmentId
""")
    List<AssessmentQuestion> findQuestionsByAssessmentId(@Param("assessmentId") UUID assessmentId);
}
