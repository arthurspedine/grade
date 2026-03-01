package com.use3w.grade.service;

import com.use3w.grade.dto.AssessmentQuestionDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentQuestion;
import com.use3w.grade.repository.AssessmentQuestionRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentQuestionService {

    private final AssessmentQuestionRepository assessmentQuestionRepository;

    public AssessmentQuestionService(AssessmentQuestionRepository assessmentQuestionRepository) {
        this.assessmentQuestionRepository = assessmentQuestionRepository;
    }

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
                                return new AssessmentQuestion(assessment, c.name(), c.score(), q.questionNumber(), c.categoryNumber());
                            }))
                    .toList();
        } catch (Exception e) {
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

    public void editCategoriesFromAssessment(Assessment assessment, List<AssessmentQuestionDTO> questions, Boolean hasAnyEvaluation) {
        List<AssessmentQuestion> existingQuestions = assessmentQuestionRepository.findQuestionsByAssessmentId(assessment.getId());

        boolean questionsChanged = hasQuestionsChanged(existingQuestions, questions);

        if (!questionsChanged) {
            // Questions are identical — nothing to do
            return;
        }

        if (hasAnyEvaluation) {
            throw new ValidationException("Não é possível alterar as questões ou notas desta avaliação pois já existem alunos avaliados.");
        }

        assessmentQuestionRepository.deleteAll(existingQuestions);
        addCategoriesToAssessment(assessment, questions);
    }

    private boolean hasQuestionsChanged(List<AssessmentQuestion> existing, List<AssessmentQuestionDTO> incoming) {
        // Flatten existing questions into comparable entries
        List<QuestionEntry> existingEntries = existing.stream()
                .map(q -> new QuestionEntry(q.getQuestionNumber(), q.getCategoryNumber(), q.getName(), q.getScore()))
                .sorted()
                .toList();

        // Flatten incoming DTO into comparable entries
        List<QuestionEntry> incomingEntries = incoming.stream()
                .flatMap(q -> q.categories().stream()
                        .map(c -> new QuestionEntry(q.questionNumber(), c.categoryNumber(), c.name(), c.score())))
                .sorted()
                .toList();

        return !existingEntries.equals(incomingEntries);
    }

    private record QuestionEntry(
            Integer questionNumber, Integer categoryNumber, String name, Double score
    ) implements Comparable<QuestionEntry> {
        @Override
        public int compareTo(QuestionEntry o) {
            int cmp = Integer.compare(this.questionNumber, o.questionNumber);
            if (cmp != 0) return cmp;
            return Integer.compare(this.categoryNumber, o.categoryNumber);
        }
    }
}
