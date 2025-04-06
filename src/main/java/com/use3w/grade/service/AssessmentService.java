package com.use3w.grade.service;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.repository.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        Assessment assessment = new Assessment(dto.name(), user.email(), classes);
        assessment = assessmentRepository.save(assessment);
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        assessmentStudentService.addStudentsToAssessment(classes, assessment);
    }

    public List<AssessmentDetailsDTO> listAssessmentsDetailsByUser(UndeterminedUser user) {
        List<Assessment> assessments = findAllByCreatedBy(user.email());
        return assessments.stream().map(this::mapperToDTO).toList();
    }

    public Assessment getAssessmentByUserAndAssessmentIdAndClassId(UndeterminedUser user, UUID id, UUID classId) {
        Assessment assessment = assessmentRepository.getAssessmentByEmailIdAndClassId(user.email(), id, classId);
        if (assessment == null)
            throw new EntityNotFoundException("Avaliação não encontrada.");
        return assessment;
    }

    private List<Assessment> findAllByCreatedBy(String createdBy) {
        return assessmentRepository.findByCreatedBy(createdBy);
    }

    private AssessmentDetailsDTO mapperToDTO(Assessment assessment) {
        return new AssessmentDetailsDTO(assessment.getId(), assessment.getName(),
                assessment.getClasses().stream()
                        .map(c -> new AssessmentDetailsDTO.AssessmentDetailsClassDTO(
                                c.getId(), c.getName(),
                                assessmentStudentService.countStudentsEvalueted(c.getId(), assessment.getId()),
                                c.getStudents().size()))
                        .collect(Collectors.toSet()));
    }
}
