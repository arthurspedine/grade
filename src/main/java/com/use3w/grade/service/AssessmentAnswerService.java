package com.use3w.grade.service;

import com.use3w.grade.model.AssessmentAnswer;
import com.use3w.grade.repository.AssessmentAnswerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AssessmentAnswerService {

    private final AssessmentAnswerRepository repository;

    public AssessmentAnswerService(AssessmentAnswerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<AssessmentAnswer> saveAssessmentAnswers(UUID studentId, List<AssessmentAnswer> answers) {
        return repository.saveAll(
                answers.stream()
                        .peek(answer -> answer.setAssessmentStudentId(studentId))
                        .toList()
        );
    }

    public Set<AssessmentAnswer> findAllByAssessmentStudentId(UUID id) {
        return repository.findAllByAssessmentStudentId(id);
    }
}
