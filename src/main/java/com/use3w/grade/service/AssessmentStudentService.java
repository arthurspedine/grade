package com.use3w.grade.service;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentClass;
import com.use3w.grade.model.AssessmentStudent;
import com.use3w.grade.model.Class;
import com.use3w.grade.repository.AssessmentStudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssessmentStudentService {

    private final AssessmentStudentRepository repository;

    public AssessmentStudentService(AssessmentStudentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void addStudentsToAssessment(List<AssessmentClass> classes) {
        Set<AssessmentStudent> studentsToAdd = new HashSet<>();
        classes.forEach(c -> c.getAssessmentClass().getStudents().forEach(
                student -> studentsToAdd.add(new AssessmentStudent(student, c))
            )
        );
        repository.saveAll(studentsToAdd);
    }

    public Long countStudentsEvalueted(Class classToSearch, Assessment assessment) {
        return repository.countEvaluatedStudents(assessment, classToSearch);
    }
}
