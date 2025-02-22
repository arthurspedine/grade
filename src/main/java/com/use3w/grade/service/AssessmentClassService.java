package com.use3w.grade.service;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentClass;
import com.use3w.grade.model.Class;
import com.use3w.grade.repository.AssessmentClassRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentClassService {
    
    private final AssessmentClassRepository repository;

    public AssessmentClassService(AssessmentClassRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public List<AssessmentClass> saveAssessmentClass(Assessment assessment, List<Class> classes) {
        List<AssessmentClass> assessmentClasses = classes.stream().map(c -> new AssessmentClass(assessment, c)).toList();
        return repository.saveAll(assessmentClasses);
    }
}
