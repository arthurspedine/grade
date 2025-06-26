package com.use3w.grade.service;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.projection.PendingAssessmentProjection;
import com.use3w.grade.repository.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final ClassService classService;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionService assessmentQuestionService;
    private final AssessmentStudentService assessmentStudentService;

    public AssessmentService(ClassService classService, AssessmentRepository assessmentRepository, AssessmentQuestionService assessmentQuestionService, AssessmentStudentService assessmentStudentService) {
        this.classService = classService;
        this.assessmentRepository = assessmentRepository;
        this.assessmentQuestionService = assessmentQuestionService;
        this.assessmentStudentService = assessmentStudentService;
    }

    @Transactional
    public void createAssessmentByUser(CreateAssessmentDTO dto, UndeterminedUser user) {
        List<Class> classes = classService.findClassesByUserAndId(user, dto.classes().stream().map(CreateAssessmentDTO.AddClassToAssessmentDTO::id).toList());
        if (classes.isEmpty())
            throw new EntityNotFoundException("Nenhuma classe encontrada.");
        Assessment assessment = new Assessment(dto.name(), user.email(), classes, LocalDate.parse(dto.assessmentDate()));
        assessment = assessmentRepository.save(assessment);
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        assessmentStudentService.addStudentsToAssessment(classes, assessment);
    }

    public List<AssessmentDetailsDTO> listAssessmentsDetailsByUser(UndeterminedUser user) {
        List<Assessment> assessments = assessmentRepository.findByCreatedByOrderByAssessmentDateAsc(user.email());
        return assessments.stream().map(this::mapperToDTO).toList();
    }

    public Assessment getAssessmentByUserAndAssessmentIdAndClassId(UndeterminedUser user, UUID id, UUID classId) {
        Assessment assessment = assessmentRepository.getAssessmentByEmailIdAndClassId(user.email(), id, classId);
        if (assessment == null)
            throw new EntityNotFoundException("Avaliação não encontrada.");
        return assessment;
    }

    public Integer countTotalAssessments(UndeterminedUser user) {
        return assessmentRepository.countByAssessmentCreatedBy(user.email());
    }

    public List<PendingAssessmentProjection> getPendingAssessments(UndeterminedUser user) {
        return assessmentRepository.findPendingAssessments(user.email(), LocalDate.now());
    }

    private AssessmentDetailsDTO mapperToDTO(Assessment assessment) {
        return new AssessmentDetailsDTO(assessment.getId(), assessment.getName(),
                assessment.getAssessmentDate().toString(),
                assessment.getClasses().stream()
                        .map(c -> new AssessmentDetailsDTO.AssessmentDetailsClassDTO(
                                c.getId(), c.getName(),
                                assessmentStudentService.countStudentsEvalueted(c.getId(), assessment.getId()),
                                c.getStudents().size()))
                        .collect(Collectors.toSet()));
    }
}
