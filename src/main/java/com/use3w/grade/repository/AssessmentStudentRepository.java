package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentStudent;
import com.use3w.grade.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AssessmentStudentRepository extends JpaRepository<AssessmentStudent, UUID> {
    @Query("SELECT COUNT(DISTINCT aSt) FROM AssessmentStudent aSt " +
            "JOIN aSt.assessment aC " +
            "WHERE aSt.finished = true " +
            "AND aC.assessment = :assessment " +
            "AND aC.assessmentClass = :class")
    Long countEvaluatedStudents(@Param("assessment") Assessment assessment,
                                @Param("class") Class classToSearch
    );

}
