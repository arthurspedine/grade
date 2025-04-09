package com.use3w.grade.controller;

import com.use3w.grade.dto.StudentEvaluationInfoDTO;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.AssessmentStudentService;
import com.use3w.grade.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/evaluate")
public class AssessmentStudentController {

    private final UserService userService;
    private final AssessmentStudentService assessmentStudentService;

    public AssessmentStudentController(UserService userService, AssessmentStudentService assessmentStudentService) {
        this.userService = userService;
        this.assessmentStudentService = assessmentStudentService;
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentEvaluationInfoDTO> getAssessmentStudentInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") UUID id) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(assessmentStudentService.getStudentEvaluationInfoDTO(user, id));
    }
}
