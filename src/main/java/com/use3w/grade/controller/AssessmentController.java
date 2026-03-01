package com.use3w.grade.controller;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.projection.PendingAssessmentProjection;
import com.use3w.grade.service.AssessmentAnswerService;
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
    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentController(UserAuthentication userAuthentication, AssessmentService assessmentService, AssessmentStudentService assessmentStudentService, AssessmentQuestionService assessmentQuestionService, AssessmentAnswerService assessmentAnswerService) {
        this.userAuthentication = userAuthentication;
        this.assessmentService = assessmentService;
        this.assessmentStudentService = assessmentStudentService;
        this.assessmentQuestionService = assessmentQuestionService;
        this.assessmentAnswerService = assessmentAnswerService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> addAssessment(
            @RequestBody @Valid CreateAssessmentDTO dto
    ) {
        String user = userAuthentication.getCurrentUser();
        Assessment assessment = assessmentService.createAssessmentByCreatedBy(dto, user);
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        assessmentStudentService.addStudentsToAssessment(assessment, assessment.getClasses());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDetailsDTO>> listAssessments() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentService.listAssessmentsDetailsByUser(user));
    }

    @GetMapping("/{id}/{classId}")
    public ResponseEntity<AssessmentInfoDTO> getAssessmentInfo(
            @PathVariable UUID id,
            @PathVariable UUID classId) {
        String user = userAuthentication.getCurrentUser();
        Assessment assessment = assessmentService.getAssessmentByUserAndAssessmentIdAndClassId(user, id, classId);
        return ResponseEntity.ok(assessmentStudentService.geAssessmentDetailsDTO(assessment, classId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PendingAssessmentProjection>> getPendingAssessments() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentService.getPendingAssessments(user));
    }

    @GetMapping("/{id}/edit-info")
    public ResponseEntity<AssessmentEditInfoDTO> getAssessmentEditInfo(@PathVariable UUID id) {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(assessmentService.getAssessmentEditInfo(user, id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> editAssessment(@PathVariable UUID id, @RequestBody EditAssessmentDTO dto) {
        String user = userAuthentication.getCurrentUser();
        boolean hasAnyEvaluation = assessmentAnswerService.hasAnyAnswerForAssessment(id);
        Assessment editedAssessment = assessmentService.editAssessment(user, id, dto.name(), dto.assessmentDate());
        assessmentQuestionService.editCategoriesFromAssessment(editedAssessment, dto.questions(), hasAnyEvaluation);
        assessmentService.editClasses(editedAssessment, dto.classes());
        return ResponseEntity.noContent().build();
    }

}
