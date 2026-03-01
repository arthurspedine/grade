package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface AssessmentAnswerRepository extends JpaRepository<AssessmentAnswer, UUID> {
    Set<AssessmentAnswer> findAllByAssessmentStudentId(UUID id);

    @Query("""
                    select
                            exists (
                                select 1
                                from AssessmentQuestion aQ
                                join AssessmentAnswer answer
                                    on answer.questionId = aQ.id
                                where aQ.assessment.id = :assessmentId
                            )
            """)
    boolean hasAnyAnswerForAssessment(@Param("assessmentId") UUID assessmentId);
}
