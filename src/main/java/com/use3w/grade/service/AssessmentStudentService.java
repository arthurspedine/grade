package com.use3w.grade.service;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.*;
import com.use3w.grade.model.Class;
import com.use3w.grade.repository.AssessmentStudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public Integer countStudentsEvalueted(UUID classId, UUID assessmentId) {
        return repository.countEvaluatedStudents(classId, assessmentId);
    }

    public AssessmentInfoDTO geAssessmentDetailsDTO(Assessment assessment) {
        List<AssessmentStudent> assessmentStudents = repository.findByAssessment(assessment);
        final int[] countEvaluatedStudents = {0};
        List<AssessmentStudentDTO> students = assessmentStudents.stream().map(assessmentStudent -> {
            boolean finished = assessmentStudent.getFinished();
            countEvaluatedStudents[0] += finished ? 1 : 0;
            Student student = assessmentStudent.getStudent();
            return new AssessmentStudentDTO(
                    new AssessmentStudentInfoDTO(assessmentStudent.getId(), student.getName(),
                    student.getRm()), assessmentStudent.getFinished()
            );
        }).toList();
        return new AssessmentInfoDTO(assessment.getName(), countEvaluatedStudents[0], students);
    }

    public StudentEvaluationInfoDTO getStudentEvaluationInfoDTO(UndeterminedUser user, UUID id) {
        AssessmentStudent assessmentStudent = repository.findByIdAndUser(user.email(), id);
        Student student = assessmentStudent.getStudent();

        if (assessmentStudent.getFinished()) {
            Set<AssessmentAnswer> assessmentAnswers = assessmentStudent.getAnswers();
            Map<Integer, List<AssessmentQuestion>> questionsByNumber = assessmentStudent.getAssessment().getQuestions().stream()
                    .collect(Collectors.groupingBy(AssessmentQuestion::getQuestionNumber));

            List<AssessmentAnswerDTO> answers = questionsByNumber.entrySet().stream()
                    .map(entry -> {
                        Integer questionNumber = entry.getKey();
                        List<CategoryAnswerDTO> categories = entry.getValue().stream()
                                .map(question -> new CategoryAnswerDTO(
                                        question.getName(), question.getScore(),
                                        assessmentAnswers.stream()
                                                .filter(answer -> answer.getQuestion().equals(question))
                                                .findFirst()
                                                .map(AssessmentAnswer::getScore).
                                                orElse(0.0)
                                )).toList();
                        return new AssessmentAnswerDTO(questionNumber, categories);
                    }).toList();

            return new StudentEvaluationInfoDTO(
                    new AssessmentStudentInfoDTO(
                            assessmentStudent.getId(), student.getName(),
                            student.getRm()), null,
                    answers, assessmentStudent.getRawFeedback(),
                            assessmentStudent.getFinalFeedback(), assessmentStudent.getTotalScore(),
                            assessmentStudent.getFinished()
            );
        } else {
            Map<Integer, List<AssessmentQuestion>> questionsByNumber = assessmentStudent.getAssessment().getQuestions().stream()
                    .collect(Collectors.groupingBy(AssessmentQuestion::getQuestionNumber));

            List<AssessmentEvaluationQuestionDTO> questions =  questionsByNumber.entrySet().stream()
                    .map(entry -> {
                        Integer questionNumber = entry.getKey();
                        List<EvaluationCategoryDTO> categories = entry.getValue().stream()
                                .map(question -> new EvaluationCategoryDTO(
                                        question.getId(), question.getName(),
                                        question.getScore()
                                )).toList();
                        return new AssessmentEvaluationQuestionDTO(questionNumber, categories);
                    })
                    .collect(Collectors.toList());
            return new StudentEvaluationInfoDTO(
                    new AssessmentStudentInfoDTO(assessmentStudent.getId(), student.getName(), student.getRm()),
                    questions, null, null, null, null, assessmentStudent.getFinished()
            );
        }
    }
}