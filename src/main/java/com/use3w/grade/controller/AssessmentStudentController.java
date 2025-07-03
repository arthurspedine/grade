package com.use3w.grade.controller;

import com.use3w.grade.dto.AssessmentStudentFinishEvaluation;
import com.use3w.grade.dto.CategoryAnswerDTO;
import com.use3w.grade.dto.GetChatFeedbackDTO;
import com.use3w.grade.dto.StudentEvaluationInfoDTO;
import com.use3w.grade.model.AssessmentAnswer;
import com.use3w.grade.service.AssessmentAnswerService;
import com.use3w.grade.service.AssessmentStudentService;
import com.use3w.grade.service.OpenAIChatService;
import com.use3w.grade.util.UserAuthentication;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/evaluate")
public class AssessmentStudentController {

    private final UserAuthentication userAuthentication;
    private final AssessmentStudentService assessmentStudentService;
    private final OpenAIChatService openAIChatService;
    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentStudentController(UserAuthentication userAuthentication, AssessmentStudentService assessmentStudentService, OpenAIChatService openAIChatService, AssessmentAnswerService assessmentAnswerService) {
        this.userAuthentication = userAuthentication;
        this.assessmentStudentService = assessmentStudentService;
        this.openAIChatService = openAIChatService;
        this.assessmentAnswerService = assessmentAnswerService;
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentEvaluationInfoDTO> getAssessmentStudentInfo(
            @PathVariable("id") UUID id) {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentStudentService.getStudentEvaluationInfoDTO(user, id));
    }

    @PostMapping("/student/{id}/finish")
    public ResponseEntity<Void> finishAssessmentStudent(
            @PathVariable("id") UUID id,
            @RequestBody AssessmentStudentFinishEvaluation dto
    ) {
        if (!dto.student().id().equals(id))
            throw new ValidationException("O ID do estudante deve ser o mesmo tanto no body, quanto na requisição.");
        String user = userAuthentication.getCurrentUser();
        List<AssessmentAnswer> answers = dto.questions().stream()
                .flatMap(question -> question.categories().stream()
                        .map(CategoryAnswerDTO::getAs)
                ).toList();
        answers = assessmentAnswerService.saveAssessmentAnswers(id, answers);
        assessmentStudentService.finishStudentEvaluation(user, id, dto, answers);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> getChatFeedback(@RequestBody GetChatFeedbackDTO dto) {
        return ResponseEntity.ok(Map.of("message", (openAIChatService.getFeedback(dto.answeredCategories(), dto.rawFeedback()))));
    }
}
