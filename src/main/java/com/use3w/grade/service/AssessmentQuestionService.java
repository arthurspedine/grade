package com.use3w.grade.service;

import com.use3w.grade.dto.AssessmentQuestionDTO;

import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentQuestion;
import com.use3w.grade.repository.AssessmentQuestionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentQuestionService {

    private final AssessmentQuestionRepository assessmentQuestionRepository;

    public AssessmentQuestionService(AssessmentQuestionRepository assessmentQuestionRepository) {
        this.assessmentQuestionRepository = assessmentQuestionRepository;
    }

    @Transactional
    public void addCategoriesToAssessment(Assessment assessment, List<AssessmentQuestionDTO> dto) {
        if (dto == null || dto.isEmpty())
            throw new NullPointerException("Lista de questões não pode ser vazia.");
        List<AssessmentQuestion> questions;
        final double[] totalScore = {0};
        try {
            questions = dto.stream()
                    .flatMap(q -> q.categories().stream()
                            .map(c -> {
                                totalScore[0] += c.score();
                                return new AssessmentQuestion(assessment, c.name(), c.score(), q.questionNumber());
                            }))
                    .toList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NullPointerException("Lista de categorias esta vazia ou algum dado invalido. Por favor, validar os dados enviados.");
        }
        assessment.getQuestions().addAll(questions);

        // check if the sum of the scores equals 100
        if (totalScore[0] != 100) {
            if (totalScore[0] > 100) {
                throw new ValidationException("A soma das notas excede o máximo: 100");
            }
            throw new ValidationException("A soma das notas deve ser igual a 100");
        }
        assessmentQuestionRepository.saveAll(questions);
    }
}
