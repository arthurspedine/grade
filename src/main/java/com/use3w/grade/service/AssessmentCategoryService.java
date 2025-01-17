package com.use3w.grade.service;

import com.use3w.grade.dto.CreateAssessmentCategoryDTO;
import com.use3w.grade.model.Assessment;
import com.use3w.grade.model.AssessmentCategory;
import com.use3w.grade.repository.AssessmentCategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentCategoryService {

    private final AssessmentCategoryRepository assessmentCategoryRepository;

    public AssessmentCategoryService(AssessmentCategoryRepository assessmentCategoryRepository) {
        this.assessmentCategoryRepository = assessmentCategoryRepository;
    }

    @Transactional
    public void addCategoriesToAssessment(Assessment assessment, List<CreateAssessmentCategoryDTO> dto) {
        List<AssessmentCategory> categories = dto.stream().map(category -> new AssessmentCategory(assessment, category.name(), category.score())).toList();
        assessment.getCategories().addAll(categories);

        // check if the sum of the scores equals 100
        int sum = categories.stream().mapToInt(AssessmentCategory::getScore).sum();
        if (sum != 100) {
            if (sum > 100) {
                throw new ValidationException("A soma das notas excede o máximo: 100");
            }
            throw new ValidationException("A soma das notas deve ser igual a 100");
        }
        assessmentCategoryRepository.saveAll(categories);
    }
}
