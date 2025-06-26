package com.use3w.grade.service;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.*;
import com.use3w.grade.repository.AssessmentStudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssessmentStudentService {

    private final AssessmentStudentRepository repository;
    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentStudentService(AssessmentStudentRepository repository, AssessmentAnswerService assessmentAnswerService) {
        this.repository = repository;
        this.assessmentAnswerService = assessmentAnswerService;
    }

    public void addStudentsToAssessment(Assessment assessment) {
        Set<AssessmentStudent> studentsToAdd = assessment.getClasses().stream()
                .flatMap(c -> c.getStudents().stream()
                        .map(student -> new AssessmentStudent(student, assessment)))
                .collect(Collectors.toSet());
        repository.saveAll(studentsToAdd);
    }

    public Integer countStudentsEvalueted(UUID classId, UUID assessmentId) {
        return repository.countEvaluatedStudents(classId, assessmentId);
    }

    public AssessmentInfoDTO geAssessmentDetailsDTO(Assessment assessment) {
        List<AssessmentStudent> assessmentStudents = repository.findByAssessmentOrderByStudent(assessment);
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

    public StudentEvaluationInfoDTO getStudentEvaluationInfoDTO(String createdBy, UUID id) {
        AssessmentStudent assessmentStudent = repository.findByIdAndUser(createdBy, id);
        Student student = assessmentStudent.getStudent();

        if (assessmentStudent.getFinished()) {
            Set<AssessmentAnswer> assessmentAnswers =  assessmentAnswerService.findAllByAssessmentStudentId(id);
            Map<Integer, List<AssessmentQuestion>> questionsByNumber = assessmentStudent.getAssessment().getQuestions().stream()
                    .collect(Collectors.groupingBy(AssessmentQuestion::getQuestionNumber));

            List<AssessmentAnswerDTO> answers = questionsByNumber.entrySet().stream()
                    .map(entry -> {
                        Integer questionNumber = entry.getKey();
                        List<CategoryAnswerDTO> categories = entry.getValue().stream()
                                .map(question -> new CategoryAnswerDTO(
                                        question.getId(), question.getName(),
                                        question.getScore(),
                                        assessmentAnswers.stream()
                                                .filter(answer -> answer.getQuestionId().equals(question.getId()))
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
                            assessmentStudent.getFinalFeedback(), assessmentStudent.getFinishedDate().toString(),
                            assessmentStudent.getTotalScore(), assessmentStudent.getFinished()
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
                    questions, null, null, null, null, null, assessmentStudent.getFinished()
            );
        }
    }

    @Transactional
    public void finishStudentEvaluation(String createdBy, UUID id, AssessmentStudentFinishEvaluation dto, List<AssessmentAnswer> answers) {
        AssessmentStudent assessmentStudent = repository.findByIdAndUser(createdBy, id);
        final double[] totalScore = {0};
        answers.forEach(answer -> totalScore[0] += answer.getScore());

        // Setting new values in the fields
        assessmentStudent.setRawFeedback(dto.rawFeedback());
        assessmentStudent.setFinalFeedback(dto.finalFeedback());
        assessmentStudent.setFinished(true);
        assessmentStudent.setTotalScore(totalScore[0]);
        assessmentStudent.setFinishedDate(LocalDate.now());
        repository.save(assessmentStudent);
    }
}