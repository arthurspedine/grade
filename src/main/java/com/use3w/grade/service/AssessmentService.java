package com.use3w.grade.service;

import com.use3w.grade.dto.AssessmentDetailsDTO;
import com.use3w.grade.dto.AssessmentInfoDTO;
import com.use3w.grade.dto.CreateAssessmentDTO;
import com.use3w.grade.dto.StudentDTO;
import com.use3w.grade.model.*;
import com.use3w.grade.model.Class;
import com.use3w.grade.repository.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final ClassService classService;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionService assessmentQuestionService;
    private final AssessmentStudentService assessmentStudentService;
    private final AssessmentClassService assessmentClassService;

    public AssessmentService(ClassService classService, AssessmentRepository assessmentRepository, AssessmentQuestionService assessmentQuestionService, AssessmentStudentService assessmentStudentService, AssessmentClassService assessmentClassService) {
        this.classService = classService;
        this.assessmentRepository = assessmentRepository;
        this.assessmentQuestionService = assessmentQuestionService;
        this.assessmentStudentService = assessmentStudentService;
        this.assessmentClassService = assessmentClassService;
    }

    @Transactional
    public void createAssessmentByUser(CreateAssessmentDTO dto, UndeterminedUser user) {
        List<Class> classes = classService.findClassesByUserAndId(user, dto.classes().stream().map(CreateAssessmentDTO.AddClassToAssessmentDTO::id).toList());
        if (classes.isEmpty())
            throw new EntityNotFoundException("Nenhuma classe encontrada.");
        Assessment assessment = new Assessment(dto.name(), user.email());
        assessment = assessmentRepository.save(assessment);
        assessmentQuestionService.addCategoriesToAssessment(assessment, dto.questions());
        List<AssessmentClass> assessmentClasses = assessmentClassService.saveAssessmentClass(assessment, classes);
        assessmentStudentService.addStudentsToAssessment(assessmentClasses);
    }

    public List<AssessmentDetailsDTO> listAssessmentsDetailsByUser(UndeterminedUser user) {
        List<Assessment> assessments = findAllByCreatedBy(user.email());
        return assessments.stream().map(this::mapperToDTO).toList();
    }

    public AssessmentInfoDTO getAssessmentInfoByUserAndAssessmentIdAndClassId(UndeterminedUser user, UUID id, UUID classId) {
        Assessment assessment = assessmentRepository.getAssessmentByEmailIdAndClassId(user.email(), id, classId);
        if (assessment == null)
            throw new EntityNotFoundException("Avaliação não encontrada.");

        AssessmentClass assessmentClass = assessment.getClasses().stream().findFirst().orElseThrow();
        return new AssessmentInfoDTO(assessment.getName(), assessmentClass.getAssessmentClass().getStudents()
                .stream()
                .map(s -> new StudentDTO(s.getRm(), s.getName())).toList()
        );
    }

    private List<Assessment> findAllByCreatedBy(String createdBy) {
        return assessmentRepository.findByCreatedBy(createdBy);
    }

    private AssessmentDetailsDTO mapperToDTO(Assessment assessment) {
        return new AssessmentDetailsDTO(assessment.getId(), assessment.getName(),
                assessment.getClasses().stream().map(assessmentClass -> {
                    Class c = assessmentClass.getAssessmentClass();
                    Set<Student> classStudents = c.getStudents();
                    return new AssessmentDetailsDTO.AssessmentDetailsClassDTO(
                            c.getId(), c.getName(),
                            assessmentStudentService.countStudentsEvalueted(c, assessment),
                            classStudents.size()
                            );
                }).collect(Collectors.toSet()));
    }
}
