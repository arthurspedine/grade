package com.use3w.grade.service;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.Student;
import com.use3w.grade.model.UndeterminedUser;
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

    public List<ClassDetailsDTO> findAllClassesByUndeterminedUser(UndeterminedUser user) {
        List<Class> classList = getAllClassesByUndeterminedUser(user);
        return classList.stream().map(this::mapperDetailsToDTO).toList();
    }

    @Transactional
    public Class createClassByUser(UndeterminedUser user, CreateClassDTO dto) {
        Class newClass = new Class();
        newClass.setName(dto.name());
        newClass.setCreatedBy(user.email());
        newClass.setCategory(dto.category());
        return classRepository.save(newClass);
    }


    @Transactional
    public Class editClass(UndeterminedUser user, EditClassDTO dto) {
        Class requestedClass = getClass(user, dto.id());
        String newName = !Objects.isNull(dto.name()) && !dto.name().isBlank() ? dto.name() : requestedClass.getName();
        ECategory newCategory = dto.category() != null ? dto.category() : requestedClass.getCategory();
        requestedClass.setName(newName);
        requestedClass.setCategory(newCategory);
        return classRepository.save(requestedClass);
    }

    public void deleteClass(UndeterminedUser user, UUID id) {
        Class requestedClass = getClass(user, id);
        requestedClass.disableClass();
        classRepository.save(requestedClass);
    }

    public ClassInfoDTO getClassInfoByUndeterminedUserAndId(UndeterminedUser user, UUID id) {
        Class requestedClass = getClass(user, id);
        return new ClassInfoDTO(mapperDetailsToDTO(requestedClass),
                requestedClass.getStudents().stream()
                        .sorted(Comparator.comparing(Student::getName))
                        .map(s -> new StudentDTO(s.getRm(), s.getName())).toList());
    }

    private ClassDetailsDTO mapperDetailsToDTO(Class c) {
        return new ClassDetailsDTO(c.getId(), c.getName(),
                c.getCategory(), c.getStatus());
    }

    private Class getClass(UndeterminedUser user, UUID id) {
        Class requestedClass = classRepository.findByIdAndCreatedByAndActiveIsTrue(id, user.email());
        if (requestedClass == null)
            throw new EntityNotFoundException("Class not found.");
        return requestedClass;
    }

    private List<Class> getAllClassesByUndeterminedUser(UndeterminedUser user) {
        return classRepository.findClassesByCreatedByAndActiveIsTrue(user.email());
    }
}
