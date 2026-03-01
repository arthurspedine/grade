package com.use3w.grade.service;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentQuestion;
import com.use3w.grade.model.Class;
import com.use3w.grade.projection.PendingAssessmentProjection;
import com.use3w.grade.repository.AssessmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final ClassService classService;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentStudentService assessmentStudentService;
    private final AssessmentAnswerService assessmentAnswerService;

    public AssessmentService(ClassService classService, AssessmentRepository assessmentRepository, AssessmentStudentService assessmentStudentService, AssessmentAnswerService assessmentAnswerService) {
        this.classService = classService;
        this.assessmentRepository = assessmentRepository;
        this.assessmentStudentService = assessmentStudentService;
        this.assessmentAnswerService = assessmentAnswerService;
    }

    @Transactional
    public Assessment createAssessmentByCreatedBy(CreateAssessmentDTO dto, String createdBy) {
        List<Class> classes = classService.findClassesByCreatedByAndId(createdBy, dto.classes().stream().map(ClassToAssessmentDTO::id).toList());
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
        boolean hasAnyEvaluation = assessmentAnswerService.hasAnyAnswerForAssessment(assessment.getId());
        return new AssessmentDetailsDTO(assessment.getId(), assessment.getName(),
                assessment.getAssessmentDate().toString(),
                !hasAnyEvaluation,
                assessment.getClasses().stream()
                        .map(c -> new AssessmentDetailsDTO.AssessmentDetailsClassDTO(
                                c.getId(), c.getName(),
                                assessmentStudentService.countStudentsEvalueted(c.getId(), assessment.getId()),
                                c.getStudents().size()))
                        .collect(Collectors.toSet()));
    }

    public AssessmentEditInfoDTO getAssessmentEditInfo(String user, UUID assessmentId) {
        Assessment assessment = assessmentRepository.getReferenceById(assessmentId);
        if (!assessment.getCreatedBy().equals(user)) {
            throw new ValidationException("Esta avaliação não te pertence.");
        }

        boolean questionsEditable = !assessmentAnswerService.hasAnyAnswerForAssessment(assessmentId);

        // Current classes with removability info
        List<AssessmentEditInfoDTO.EditableClassDTO> currentClasses = assessment.getClasses().stream()
                .map(c -> {
                    boolean hasEvaluation = assessmentStudentService.hasEvaluationAlreadyStartedByClassAndAssessment(c.getId(), assessmentId);
                    return new AssessmentEditInfoDTO.EditableClassDTO(c.getId(), c.getName(), !hasEvaluation);
                })
                .toList();

        // Available classes (user's active classes not already in this assessment)
        Set<UUID> currentClassIds = assessment.getClasses().stream().map(Class::getId).collect(Collectors.toSet());
        List<AssessmentEditInfoDTO.AvailableClassDTO> availableClasses = classService.findAllClassesByCreatedBy(user).stream()
                .filter(c -> !currentClassIds.contains(c.id()))
                .map(c -> new AssessmentEditInfoDTO.AvailableClassDTO(c.id(), c.name()))
                .toList();

        // Current questions grouped by questionNumber
        Map<Integer, List<AssessmentQuestion>> questionsByNumber = assessment.getQuestions().stream()
                .collect(Collectors.groupingBy(AssessmentQuestion::getQuestionNumber));

        List<AssessmentQuestionDTO> questions = questionsByNumber.entrySet().stream()
                .map(entry -> new AssessmentQuestionDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(q -> new CategoryDTO(q.getName(), q.getScore(), q.getCategoryNumber()))
                                .toList()
                ))
                .toList();

        return new AssessmentEditInfoDTO(
                assessment.getName(),
                assessment.getAssessmentDate().toString(),
                questionsEditable,
                currentClasses,
                availableClasses,
                questions
        );
    }

    public Assessment editAssessment(String user, UUID id, String name, String assessmentDate) {
        Assessment assessment = assessmentRepository.getReferenceById(id);
        if (!assessment.getCreatedBy().equals(user)) {
            throw new ValidationException("Esta avaliação não te pertence.");
        }
        if (!assessment.getName().equals(name)) {
            assessment.setName(name);
        }

        LocalDate localDateAssessmentDate = LocalDate.parse(assessmentDate);
        if (!assessment.getAssessmentDate().equals(localDateAssessmentDate)) {
            assessment.setAssessmentDate(localDateAssessmentDate);
        }

        return assessmentRepository.save(assessment);
    }

    public void editClasses(Assessment assessment, List<ClassToAssessmentDTO> classes) {
        Set<Class> classesToRemove = identifyClassesToRemove(assessment, classes);

        // Validate first: check if any class to remove has evaluations for THIS assessment
        StringBuilder sb = new StringBuilder();
        for (Class c : classesToRemove) {
            boolean hasEvaluatedStudent = assessmentStudentService.hasEvaluationAlreadyStartedByClassAndAssessment(c.getId(), assessment.getId());
            if (hasEvaluatedStudent) {
                sb.append("A turma ").append(c.getName()).append(" possui alunos avaliados e não pode ser removida. ");
            }
        }
        if (!sb.isEmpty()) {
            throw new ValidationException(sb.toString());
        }

        // Only after validation passes, perform removals
        if (!classesToRemove.isEmpty()) {
            assessmentStudentService.removeStudentsFromAssessment(assessment, classesToRemove);
            assessment.getClasses().removeAll(classesToRemove);
        }

        // Add new classes
        Set<Class> newClasses = identifyClassesToAdd(assessment, classes);
        if (!newClasses.isEmpty()) {
            assessment.getClasses().addAll(newClasses);
            assessmentStudentService.addStudentsToAssessment(assessment, newClasses);
        }

        assessmentRepository.save(assessment);
    }

    private Set<Class> identifyClassesToAdd(Assessment assessment, List<ClassToAssessmentDTO> classes) {
        List<Class> classesToAdd = classService.findClassesByCreatedByAndId(assessment.getCreatedBy(), classes.stream().map(ClassToAssessmentDTO::id).collect(Collectors.toList()));
        return classesToAdd.stream().filter(
                c -> !assessment.getClasses().contains(c)
        ).collect(Collectors.toSet());
    }

    private Set<Class> identifyClassesToRemove(Assessment assessment, List<ClassToAssessmentDTO> classes) {
        return assessment.getClasses().stream().filter(
                c -> !classes.contains(new ClassToAssessmentDTO(c.getId()))
        ).collect(Collectors.toSet());
    }
}
