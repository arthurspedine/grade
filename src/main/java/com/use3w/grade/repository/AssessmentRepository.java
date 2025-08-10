package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.projection.PendingAssessmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    @Query("""
        select a from Assessment a
        join fetch a.classes c
        where a.createdBy = :createdBy
        and c.active = true
        order by a.assessmentDate asc
    """)
    List<Assessment> findByCreatedByOrderByAssessmentDateAsc(String createdBy);

    @Query("""
                    select a from Assessment a
                    join AssessmentStudent aSt on aSt.assessment.id = a.id and aSt.classId = :classId
                    join Class c on c.id = aSt.classId and c.active = true
                    where a.id = :id and a.createdBy = :createdBy
            """)
    Assessment getAssessmentByCreatedByIdAndClassId(
            @Param("createdBy") String createdBy,
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
                       COUNT(DISTINCT ast.class_id) as classesCount
                   FROM assessments a
                   JOIN assessments_students ast ON ast.assessment_id = a.id
                   JOIN classes c ON c.id = ast.class_id AND c.active = true
                   WHERE
                       a.created_by = :createdBy
                       AND ast.finished = false
                   GROUP BY
                       a.id, a.name, a.assessment_date
                   HAVING
                       COUNT(DISTINCT ast.class_id) > 0
                   ORDER BY
                       a.assessment_date ASC,
                       a.name ASC
                   LIMIT 5
            """, nativeQuery = true)
    List<PendingAssessmentProjection> findPendingAssessments(@Param("createdBy") String createdBy);
}
