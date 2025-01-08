package com.use3w.grade.service;

import com.use3w.grade.dto.StudentDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.Student;
import com.use3w.grade.repository.StudentRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository repository, StudentRepository studentRepository) {
        this.repository = repository;
        this.studentRepository = studentRepository;
    }

    public void addStudentsToClass(Class newClass, @NotNull List<StudentDTO> students) {
        Map<String, String> rmMap = students.stream()
                .collect(Collectors.toMap(StudentDTO::rm, StudentDTO::name));

        // Getting the existingStudents from db
        List<Student> existingStudents = repository.findByRmIn(new ArrayList<>(rmMap.keySet()));

        // Filter those aren't saved in db
        List<String> missingRms = new ArrayList<>(rmMap.keySet());
        existingStudents.forEach(s -> missingRms.remove(s.getRm()));

        // Creating the new students and saving
        List<Student> newStudents = new ArrayList<>();
        for (String rm : missingRms) {
            Student student = new Student();
            student.setRm(rm);
            student.setName(rmMap.get(rm));
            newStudents.add(student);
        }
        repository.saveAll(newStudents);

        // Merge the lists to add each to the class
        existingStudents.addAll(newStudents);

        for (Student student : existingStudents) {
            student.getClasses().add(newClass);
            newClass.getStudents().add(student);
        }

        studentRepository.saveAll(existingStudents);
    }
}
