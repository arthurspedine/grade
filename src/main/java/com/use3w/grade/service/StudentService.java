package com.use3w.grade.service;

import com.use3w.grade.model.Class;
import com.use3w.grade.model.Student;
import com.use3w.grade.repository.StudentRepository;
import com.use3w.grade.util.csv.CsvReader;
import com.use3w.grade.util.csv.StudentCsvReader;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final CsvReader<Student> reader;

    public StudentService(StudentRepository repository, StudentCsvReader reader) {
        this.repository = repository;
        this.reader = reader;
    }

    @Transactional
    public void addStudentsToClass(Class newClass, MultipartFile csvFile) {
        try {
            List<Student> studentsFromCsv = reader.readFromCsv(csvFile);
            Map<String, Student> csvStudentsMap = studentsFromCsv.stream()
                    .collect(Collectors.toMap(Student::getRm, student -> student));

            StudentsMap studentsMap = studentsSetup(csvStudentsMap);

            // Merge the lists to add each to the class
            List<Student> allStudents = Stream.concat(studentsMap.existingStudents.stream(), studentsMap.newStudents.stream())
                    .collect(Collectors.toList());

            addStudentsListToClass(allStudents, newClass);

            repository.saveAll(allStudents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void editStudentsFromClass(Class editedClass, MultipartFile file) {
        if (file == null || file.isEmpty())
            return;
        try {
            List<Student> studentsFromCsv = reader.readFromCsv(file);
            Map<String, Student> csvStudentsMap = studentsFromCsv.stream()
                    .collect(Collectors.toMap(Student::getRm, student -> student));

            // get students that need to be removed because aren't in csv
            List<Student> studentsToRemove = editedClass.getStudents().stream()
                    .filter(student -> !csvStudentsMap.containsKey(student.getRm()))
                    .toList();

            removeStudentsFromClass(studentsToRemove, editedClass);

            StudentsMap studentsMap = studentsSetup(csvStudentsMap);

            List<Student> studentsToAdd = Stream.concat(studentsMap.existingStudents.stream()
                                    .filter(student -> !editedClass.getStudents().contains(student)),
                            studentsMap.newStudents.stream())
                    .toList();

            addStudentsListToClass(studentsToAdd, editedClass);

            repository.saveAll(studentsToRemove);
            repository.saveAll(studentsToAdd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record StudentsMap(
            List<Student> existingStudents, List<Student> newStudents
    ) {}

    private StudentsMap studentsSetup(Map<String, Student> fromMap) {
        // get students from database that are in csv
        List<Student> existingStudents = repository.findByRmIn(new ArrayList<>(fromMap.keySet()));
        Map<String, Student> existingStudentsMap = existingStudents.stream()
                .collect(Collectors.toMap(Student::getRm, student -> student));

        // Creating the new students and saving
        List<Student> newStudents = creatingNewStudents(fromMap, existingStudentsMap);

        if (!newStudents.isEmpty()) {
            repository.saveAll(newStudents);
        }
        return new StudentsMap(existingStudents, newStudents);
    }

    private List<Student> creatingNewStudents(Map<String, Student> csvStudentsMap, Map<String, Student> existingStudentsMap) {
        return csvStudentsMap.entrySet().stream()
                .filter(entry -> !existingStudentsMap.containsKey(entry.getKey()))
                .map(entry -> {
                    Student student = new Student();
                    student.setRm(entry.getKey());
                    student.setName(entry.getValue().getName());
                    return student;
                })
                .collect(Collectors.toList());
    }

    private void addStudentsListToClass(List<Student> studentsToAdd, Class classToBeAdded) {
        studentsToAdd.forEach(student -> {
            student.getClasses().add(classToBeAdded);
            classToBeAdded.getStudents().add(student);
        });
    }

    private void removeStudentsFromClass(List<Student> studentsToRemove, Class classToBeRemoved) {
        studentsToRemove.forEach(student -> {
            student.getClasses().remove(classToBeRemoved);
            classToBeRemoved.getStudents().remove(student);
        });
    }
}
