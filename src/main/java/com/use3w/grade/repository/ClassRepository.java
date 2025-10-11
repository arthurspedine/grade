package com.use3w.grade.repository;

import com.use3w.grade.dto.ClassPerformanceDTO;
import com.use3w.grade.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<Class, UUID> {

    List<Class> findClassesByCreatedByAndActiveIsTrueOrderByNameAsc(String createdBy);

    Class findByIdAndCreatedByAndActiveIsTrue(UUID id, String createdBy);

    @Query("SELECT c FROM Class c WHERE c.id IN :ids AND c.active = TRUE AND c.createdBy = :createdBy")
    List<Class> findClassesByIdInAndActiveIsTrueAndCreatedBy(@Param("ids") List<UUID> ids, @Param("createdBy") String createdBy);

    @Query("""
            SELECT COUNT(c)
            FROM Class c
            WHERE c.createdBy = :createdBy
            AND c.active = true
            """)
    Integer countByClassCreatedBy(@Param("createdBy") String createdBy);

    @Query("""
        SELECT new com.use3w.grade.dto.ClassPerformanceDTO(
            c.id,
            c.name,
            ROUND(COALESCE(AVG(CASE WHEN aSt.finished = true THEN aSt.totalScore ELSE NULL END), 0.0), 2)
        )
        FROM Class c
        JOIN c.students s
        LEFT JOIN s.assessments aSt
        ON aSt.classId = c.id
        WHERE c.createdBy = :createdBy and c.active = true
        GROUP BY c.id
        ORDER BY
            ROUND(COALESCE(AVG(CASE WHEN aSt.finished = true THEN aSt.totalScore ELSE NULL END), 0.0), 2) DESC,
            c.name
        LIMIT 3
""")
    List<ClassPerformanceDTO> getClassesPerformance(@Param("createdBy") String createdBy);

    @Query("SELECT c.name FROM Class c WHERE c.id = :classId AND c.active = TRUE")
    String findNameById(@Param("classId") UUID classId);
}
