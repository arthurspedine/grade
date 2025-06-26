package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssessmentStudentRepository extends JpaRepository<AssessmentStudent, UUID> {

    @Query(value = """
            select COUNT(DISTINCT aSt.student_rm) from assessments_students aSt
            inner join assessments a on aSt.assessment_id = a.id
            inner join assessments_classes ac on ac.assessment_id = a.id
            where ac.class_id = :classId and aSt.finished = true and aSt.assessment_id = :assessmentId""", nativeQuery = true)
    Integer countEvaluatedStudents(@Param("classId") UUID classId, @Param("assessmentId") UUID assessmentId);

    @Query("select a from AssessmentStudent a where a.assessment = ?1 order by a.student.name ASC")
    List<AssessmentStudent> findByAssessmentOrderByStudent(Assessment assessment);

    @Query(value = """
                select aSt from AssessmentStudent aSt
                inner join Assessment a on aSt.assessment.createdBy = :createdBy
                where aSt.id = :id
            """)
    AssessmentStudent findByIdAndUser(@Param("createdBy") String createdBy, @Param("id") UUID id);

}
