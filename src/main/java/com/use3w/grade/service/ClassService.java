package com.use3w.grade.service;

import com.use3w.grade.dto.ClassDetailsDTO;
import com.use3w.grade.dto.ClassInfoDTO;
import com.use3w.grade.dto.ClassPerformanceDTO;
import com.use3w.grade.dto.StudentDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.Student;
import com.use3w.grade.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassDetailsDTO> findAllClassesByUndeterminedUser(String createdBy) {
        List<Class> classList = getAllClassesByUndeterminedUser(createdBy);
        return classList.stream().map(this::mapperDetailsToDTO).toList();
    }

    @Transactional
    public Class createClassByUser(String name, String createdBy, ECategory category) {
        Class newClass = new Class(name, createdBy, category);
        return classRepository.save(newClass);
    }


    @Transactional
    public Class editClass(String createdBy, Class editedClass) {
        Class requestedClass = getClass(createdBy, editedClass.getId());

        String editedName = editedClass.getName();
        String newName = !Objects.isNull(editedName) && !editedName.isBlank() ? editedName : requestedClass.getName();

        ECategory newCategory = editedClass.getCategory() != null ? editedClass.getCategory() : requestedClass.getCategory();

        requestedClass.setName(newName);
        requestedClass.setCategory(newCategory);
        return classRepository.save(requestedClass);
    }

    public void deleteClass(String createdBy, UUID id) {
        Class requestedClass = getClass(createdBy, id);
        requestedClass.disableClass();
        classRepository.save(requestedClass);
    }

    public ClassInfoDTO getClassInfoByUndeterminedUserAndId(String createdBy, UUID id) {
        Class requestedClass = getClass(createdBy, id);
        return new ClassInfoDTO(mapperDetailsToDTO(requestedClass),
                requestedClass.getStudents().stream()
                        .sorted(Comparator.comparing(Student::getName))
                        .map(s -> new StudentDTO(s.getRm(), s.getName())).toList());
    }

    public List<Class> findClassesByUserAndId(String createdBy, List<UUID> ids) {
        return classRepository.findClassesByIdInAndActiveIsTrueAndCreatedBy(ids, createdBy);
    }

    public Integer countTotalClasses(String createdBy) {
        return classRepository.countByClassCreatedBy(createdBy);
    }

    public List<ClassPerformanceDTO> getClassesPerformance(String createdBy) {
        return classRepository.getClassesPerformance(createdBy);
    }

    private ClassDetailsDTO mapperDetailsToDTO(Class c) {
        return new ClassDetailsDTO(c.getId(), c.getName(),
                c.getCategory(), c.getStatus());
    }

    private Class getClass(String createdBy, UUID id) {
        Class requestedClass = classRepository.findByIdAndCreatedByAndActiveIsTrue(id, createdBy);
        if (requestedClass == null)
            throw new EntityNotFoundException("Class not found.");
        return requestedClass;
    }

    private List<Class> getAllClassesByUndeterminedUser(String createdBy) {
        return classRepository.findClassesByCreatedByAndActiveIsTrueOrderByNameAsc(createdBy);
    }
}
