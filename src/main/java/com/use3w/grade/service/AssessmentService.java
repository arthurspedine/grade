package com.use3w.grade.service;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.Class;
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
    private final AssessmentStudentService assessmentStudentService;

    public AssessmentService(ClassService classService, AssessmentRepository assessmentRepository, AssessmentStudentService assessmentStudentService) {
        this.classService = classService;
        this.assessmentRepository = assessmentRepository;
        this.assessmentStudentService = assessmentStudentService;
    }

    @Transactional
    public Assessment createAssessmentByCreatedBy(CreateAssessmentDTO dto, String createdBy) {
        List<Class> classes = classService.findClassesByCreatedByAndId(createdBy, dto.classes().stream().map(CreateAssessmentDTO.AddClassToAssessmentDTO::id).toList());
        if (classes.isEmpty())
            throw new EntityNotFoundException("Nenhuma classe encontrada.");
        Assessment assessment = new Assessment(dto.name(), createdBy, classes, LocalDate.parse(dto.assessmentDate()));
        return assessmentRepository.save(assessment);
    }

    public List<AssessmentDetailsDTO> listAssessmentsDetailsByUser(String createdBy) {
        List<Assessment> assessments = assessmentRepository.findByCreatedByOrderByAssessmentDateAsc(createdBy);
        return assessments.stream().map(this::mapperToDTO).toList();
    }

    public Assessment getAssessmentByUserAndAssessmentIdAndClassId(String createdBy, UUID id, UUID classId) {
        Assessment assessment = assessmentRepository.getAssessmentByCreatedByIdAndClassId(createdBy, id, classId);
        if (assessment == null)
            throw new EntityNotFoundException("Avaliação não encontrada.");
        return assessment;
    }

    public Integer countTotalAssessments(String createdBy) {
        return assessmentRepository.countByAssessmentCreatedBy(createdBy);
    }

    public List<PendingAssessmentProjection> getPendingAssessments(String createdBy) {
        return assessmentRepository.findPendingAssessments(createdBy);
    }

    private AssessmentDetailsDTO mapperToDTO(Assessment assessment) {
        if (assessment.getClasses().isEmpty()) {
            return null;
        }
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
