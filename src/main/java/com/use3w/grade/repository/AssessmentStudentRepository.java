package com.use3w.grade.repository;

import com.use3w.grade.model.AssessmentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AssessmentStudentRepository extends JpaRepository<AssessmentStudent, UUID> {

    @Query(value = """
            select COUNT(DISTINCT aSt.student_rm) from assessments_students aSt
            inner join assessments a on aSt.assessment_id = a.id
            inner join assessments_classes ac on ac.assessment_id = a.id
            where ac.class_id = :classId and aSt.finished = true and aSt.assessment_id = :assessmentId""", nativeQuery = true)
    Long countEvaluatedStudents(@Param("classId") UUID classId, @Param("assessmentId") UUID assessmentId);
}
