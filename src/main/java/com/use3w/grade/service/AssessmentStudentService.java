package com.use3w.grade.service;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentStudent;
import com.use3w.grade.model.Class;
import com.use3w.grade.repository.AssessmentStudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssessmentStudentService {

    private final AssessmentStudentRepository repository;

    public AssessmentStudentService(AssessmentStudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addStudentsToAssessment(List<Class> classes, Assessment assessment) {
        Set<AssessmentStudent> studentsToAdd = classes.stream()
                .flatMap(c -> c.getStudents().stream()
                        .map(student -> new AssessmentStudent(student, assessment)))
                .collect(Collectors.toSet());
        repository.saveAll(studentsToAdd);
    }

    public Long countStudentsEvalueted(UUID classId, UUID assessmentId) {
        return repository.countEvaluatedStudents(classId, assessmentId);
    }
}