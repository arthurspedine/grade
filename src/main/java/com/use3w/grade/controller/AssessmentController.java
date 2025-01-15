package com.use3w.grade.controller;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.AssessmentCategoryService;
import com.use3w.grade.service.AssessmentService;
import com.use3w.grade.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {


    private final UserService userService;
    private final AssessmentService assessmentService;

    public AssessmentController(UserService userService, AssessmentService assessmentService) {
        this.userService = userService;
        this.assessmentService = assessmentService;
    }

    @PostMapping
    private ResponseEntity<Void> addAssessment(
            @RequestBody CreateAssessmentDTO dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        assessmentService.createAssessmentByUser(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<?> listAssessments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        List<AssessmentDetailsDTO> assessments = assessmentService.listAssessmentsDetailsByUser(user);
        return ResponseEntity.ok(assessments);
    }
}
