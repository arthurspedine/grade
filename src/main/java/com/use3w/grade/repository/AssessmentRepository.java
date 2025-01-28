package com.use3w.grade.repository;

import com.use3w.grade.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByCreatedBy(String createdBy);

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
}
