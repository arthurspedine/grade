package com.use3w.grade.repository;

import com.use3w.grade.model.Student;
import com.use3w.grade.projection.StudentClassProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByRmIn(List<String> rms);

    @Query("""
                SELECT s.rm as rm, c.name as name
                FROM Student s
                JOIN s.classes c
                WHERE c.createdBy = :createdBy
                AND c.active = true
                AND s IN :students
            """)
    List<StudentClassProjection> findByClassCreatedBy(@Param("createdBy") String createdBy, @Param("students") List<Student> students);
}
