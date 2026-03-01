package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentStudent;
import com.use3w.grade.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AssessmentStudentRepository extends JpaRepository<AssessmentStudent, UUID> {

    @Query(value = """
            select COUNT(DISTINCT aSt.student_rm) from assessments_students aSt
            where aSt.class_id = :classId and aSt.finished = true and aSt.assessment_id = :assessmentId""", nativeQuery = true)
    Integer countEvaluatedStudents(@Param("classId") UUID classId, @Param("assessmentId") UUID assessmentId);

    @Query("select a from AssessmentStudent a where a.assessment = :assessment and a.classId = :classId order by a.student.name ASC")
    List<AssessmentStudent> findByAssessmentOrderByStudent(@Param("assessment") Assessment assessment, @Param("classId") UUID classId);

    @Query(value = """
                select aSt from AssessmentStudent aSt
                inner join Assessment a on aSt.assessment.createdBy = :createdBy
                where aSt.id = :id
            """)
    AssessmentStudent findByIdAndUser(@Param("createdBy") String createdBy, @Param("id") UUID id);

    @Query("""
        select
                exists (
                    select 1
                    from AssessmentStudent aSt
                    join AssessmentAnswer answer
                        on answer.assessmentStudentId = aSt.id
                    where aSt.classId = :classId
                    and aSt.assessment.id = :assessmentId
                )
""")
    boolean hasEvaluationAlreadyStartedByClassAndAssessment(@Param("classId") UUID classId, @Param("assessmentId") UUID assessmentId);

    @Query(value = """
        select aSt 
           from AssessmentStudent aSt
           where aSt.assessment.id = :assessmentId
           and aSt.classId in (:classes)
   """)
    Set<AssessmentStudent> findAssessmentStudentByAssessmentAndClasses(
            @Param("assessmentId") UUID assessmentId,
            @Param("classes") Set<UUID> classes
    );

    @Query("""
        select aSt from AssessmentStudent aSt
        where aSt.assessment.id = :assessmentId
        and aSt.classId = :classId
        and aSt.student.rm in (:studentRms)
    """)
    Set<AssessmentStudent> findByAssessmentAndClassAndStudents(
            @Param("assessmentId") UUID assessmentId,
            @Param("classId") UUID classId,
            @Param("studentRms") Set<String> studentRms
    );

    @Query("""
        select aSt.assessment from AssessmentStudent aSt
        where aSt.classId = :classId
        group by aSt.assessment
    """)
    List<Assessment> findAssessmentsByClassId(@Param("classId") UUID classId);
}
