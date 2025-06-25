package com.use3w.grade.controller;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.AssessmentAnswer;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.AssessmentAnswerService;
import com.use3w.grade.service.AssessmentStudentService;
import com.use3w.grade.service.OpenAIChatService;
import com.use3w.grade.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/evaluate")
public class AssessmentStudentController {

    private final UserService userService;
    private final AssessmentStudentService assessmentStudentService;
    private final OpenAIChatService openAIChatService;
    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentStudentController(UserService userService, AssessmentStudentService assessmentStudentService, OpenAIChatService openAIChatService, AssessmentAnswerService assessmentAnswerService) {
        this.userService = userService;
        this.assessmentStudentService = assessmentStudentService;
        this.openAIChatService = openAIChatService;
        this.assessmentAnswerService = assessmentAnswerService;
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentEvaluationInfoDTO> getAssessmentStudentInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") UUID id) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(assessmentStudentService.getStudentEvaluationInfoDTO(user.email(), id));
    }

    @PostMapping("/student/{id}/finish")
    public ResponseEntity<Void> finishAssessmentStudent(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") UUID id,
            @RequestBody AssessmentStudentFinishEvaluation dto
    ) {
        if (!dto.student().id().equals(id))
            throw new ValidationException("O ID do estudante deve ser o mesmo tanto no body, quanto na requisição.");
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        List<AssessmentAnswer> answers = dto.questions().stream()
                .flatMap(question -> question.categories().stream()
                        .map(CategoryAnswerDTO::getAs)
                ).toList();
        answers = assessmentAnswerService.saveAssessmentAnswers(id, answers);
        assessmentStudentService.finishStudentEvaluation(user.email(), id, dto, answers);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> getChatFeedback(@RequestBody GetChatFeedbackDTO dto) {
        return ResponseEntity.ok(Map.of("message", (openAIChatService.getFeedback(dto.answeredCategories(), dto.rawFeedback()))));
    }
}
