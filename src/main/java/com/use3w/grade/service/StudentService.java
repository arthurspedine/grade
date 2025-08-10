package com.use3w.grade.service;

import com.use3w.grade.model.Class;
import com.use3w.grade.model.Student;
import com.use3w.grade.projection.StudentClassProjection;
import com.use3w.grade.repository.StudentRepository;
import com.use3w.grade.util.csv.CsvReader;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final CsvReader<Student> reader;

    public StudentService(StudentRepository repository, CsvReader<Student> reader) {
        this.repository = repository;
        this.reader = reader;
    }

    @Transactional
    public void addStudentsToClass(Class newClass, MultipartFile csvFile) {
        try {
            List<Student> studentsFromCsv = reader.readFromCsv(csvFile);
            Map<String, Student> csvStudentsMap = studentsFromCsv.stream()
                    .collect(Collectors.toMap(Student::getRm, Function.identity()));

            // Validate the students, if they are in another class with the same createdBy and category
            validateStudents(newClass, csvStudentsMap);

            StudentsMap studentsMap = studentsSetup(csvStudentsMap);

            // Merge the lists to add each to the class
            List<Student> allStudents = new ArrayList<>(studentsMap.existingStudents());
            allStudents.addAll(studentsMap.newStudents());

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

            // Validate the students, if they are in another class with the same createdBy
            validateStudents(editedClass, csvStudentsMap);

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

    public Integer countTotalStudents(String createdBy) {
        return repository.countByClassCreatedBy(createdBy);
    }

    private void validateStudents(Class validateClass, Map<String, Student> csvStudentsMap) {
        List<Student> csvStudents = new ArrayList<>(csvStudentsMap.values());
        List<StudentClassProjection> violatingStudents = repository.findByClassCreatedByAndCategory(
                validateClass.getCreatedBy(),
                csvStudents,
                validateClass.getName(),
                validateClass.getCategory()
        );
        if (!violatingStudents.isEmpty()) {
            throw new ValidationException("Os seguintes estudantes já estão registrados em outras turmas criadas pelo mesmo usuário: " +
                                          violatingStudents.stream()
                                                  .map(StudentClassProjection::getStudentClass)
                                                  .collect(Collectors.joining(", ")));
        }
    }

    private record StudentsMap(
            List<Student> existingStudents, List<Student> newStudents
    ) {
    }

    private StudentsMap studentsSetup(Map<String, Student> fromMap) {
        // get students from database that are in csv
        List<String> rms = new ArrayList<>(fromMap.keySet());
        List<Student> existingStudents = repository.findByRmIn(rms);
        Map<String, Student> existingStudentsMap = existingStudents.stream()
                .collect(Collectors.toMap(Student::getRm, Function.identity()));

        // Creating the new students and saving
        List<Student> newStudents = fromMap.entrySet().stream()
                .filter(entry -> !existingStudentsMap.containsKey(entry.getKey()))
                .map(entry -> {
                    Student student = new Student();
                    student.setRm(entry.getKey());
                    student.setName(entry.getValue().getName());
                    return student;
                })
                .collect(Collectors.toList());

        return new StudentsMap(existingStudents, newStudents);
    }

    private void addStudentsListToClass(List<Student> studentsToAdd, Class classToBeAdded) {
        Set<Student> studentSet = classToBeAdded.getStudents();
        studentsToAdd.forEach(student -> {
            student.getClasses().add(classToBeAdded);
            studentSet.add(student);
        });
    }

    private void removeStudentsFromClass(List<Student> studentsToRemove, Class classToBeRemoved) {
        Set<Student> studentSet = classToBeRemoved.getStudents();
        studentsToRemove.forEach(student -> {
            student.getClasses().remove(classToBeRemoved);
            studentSet.remove(student);
        });
    }
}
