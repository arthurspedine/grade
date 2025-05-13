package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.projection.PendingAssessmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByCreatedByOrderByAssessmentDateAsc(String createdBy);

    @Query("""
                    select a from Assessment a
                    join a.classes c
                    where a.id = :id and c.id = :classId and a.createdBy = :email
            """)
    Assessment getAssessmentByEmailIdAndClassId(
            @Param("email") String email,
            @Param("id") UUID id,
            @Param("classId") UUID classId
    );

    @Query("""
                    SELECT COUNT(a)
                    FROM Assessment a
                    WHERE a.createdBy = :createdBy
            """)
    Integer countByAssessmentCreatedBy(@Param("createdBy") String createdBy);

    @Query(value = """
        SELECT
            BIN_TO_UUID(a.id) as id,
            a.name,
            a.assessment_date as assessmentDate,
            COUNT(DISTINCT sc.class_id) as classesCount
        FROM assessments a
        JOIN assessments_students ast
            ON ast.assessment_id = a.id
        JOIN students s
            ON ast.student_rm = s.rm
        JOIN students_classes sc
            ON s.rm = sc.student_rm
        WHERE
            a.created_by = :createdBy
            AND ast.finished = false
        GROUP BY
            a.id, a.name, a.assessment_date
        ORDER BY
            a.assessment_date ASC
        LIMIT 3
        """, nativeQuery = true)
    List<PendingAssessmentProjection> findPendingAssessments(@Param("createdBy") String createdBy, @Param("today") LocalDate today);
}
