package com.use3w.grade.controller;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.AssessmentInfoDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.projection.PendingAssessmentProjection;
import com.use3w.grade.service.AssessmentQuestionService;
import com.use3w.grade.service.AssessmentService;
import com.use3w.grade.service.AssessmentStudentService;
import com.use3w.grade.util.UserAuthentication;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final UserAuthentication userAuthentication;
    private final AssessmentService assessmentService;
    private final AssessmentStudentService assessmentStudentService;
    private final AssessmentQuestionService assessmentQuestionService;

    public AssessmentController(UserAuthentication userAuthentication, AssessmentService assessmentService, AssessmentStudentService assessmentStudentService, AssessmentQuestionService assessmentQuestionService) {
        this.userAuthentication = userAuthentication;
        this.assessmentService = assessmentService;
        this.assessmentStudentService = assessmentStudentService;
        this.assessmentQuestionService = assessmentQuestionService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> addAssessment(
            @RequestBody @Valid CreateAssessmentDTO dto
    ) {
        String user = userAuthentication.getCurrentUser();
        Assessment assessment = assessmentService.createAssessmentByCreatedBy(dto, user);
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        assessmentStudentService.addStudentsToAssessment(assessment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDetailsDTO>> listAssessments() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentService.listAssessmentsDetailsByUser(user));
    }

    @GetMapping("/{id}/{classId}")
    public ResponseEntity<AssessmentInfoDTO> getAssessmentInfo(
            @PathVariable("id") UUID id,
            @PathVariable("classId") UUID classId) {
        String user = userAuthentication.getCurrentUser();
        Assessment assessment = assessmentService.getAssessmentByUserAndAssessmentIdAndClassId(user, id, classId);
        return ResponseEntity.ok(assessmentStudentService.geAssessmentDetailsDTO(assessment, classId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAssessmentProjection>> getPendingAssessments() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentService.getPendingAssessments(user));
    }

}
