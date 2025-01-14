package com.use3w.grade.repository;

import com.use3w.grade.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByRmIn(List<String> rms);
}
