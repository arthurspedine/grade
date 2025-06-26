package com.use3w.grade.controller;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.AssessmentInfoDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.projection.PendingAssessmentProjection;
import com.use3w.grade.service.AssessmentQuestionService;
import com.use3w.grade.service.AssessmentService;
import com.use3w.grade.service.AssessmentStudentService;
import com.use3w.grade.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final UserService userService;
    private final AssessmentService assessmentService;
    private final AssessmentStudentService assessmentStudentService;
    private final AssessmentQuestionService assessmentQuestionService;

    public AssessmentController(UserService userService, AssessmentService assessmentService, AssessmentStudentService assessmentStudentService, AssessmentQuestionService assessmentQuestionService) {
        this.userService = userService;
        this.assessmentService = assessmentService;
        this.assessmentStudentService = assessmentStudentService;
        this.assessmentQuestionService = assessmentQuestionService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> addAssessment(
            @RequestBody @Valid CreateAssessmentDTO dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Assessment assessment = assessmentService.createAssessmentByUser(dto, user.email());
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        assessmentStudentService.addStudentsToAssessment(assessment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDetailsDTO>> listAssessments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(assessmentService.listAssessmentsDetailsByUser(user.email()));
    }

    @GetMapping("/{id}/{classId}")
    public ResponseEntity<AssessmentInfoDTO> getAssessmentInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable("id") UUID id,
            @PathVariable("classId") UUID classId) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Assessment assessment = assessmentService.getAssessmentByUserAndAssessmentIdAndClassId(user.email(), id, classId);
        return ResponseEntity.ok(assessmentStudentService.geAssessmentDetailsDTO(assessment));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAssessmentProjection>> getPendingAssessments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(assessmentService.getPendingAssessments(user.email()));
    }

}
