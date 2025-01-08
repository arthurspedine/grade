package com.use3w.grade.service;

import com.use3w.grade.dto.ClassDetailsDTO;
import com.use3w.grade.dto.CreateClassDTO;
import com.use3w.grade.dto.EditClassDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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
        return classList.stream().map(c -> new ClassDetailsDTO(c.getId(), c.getName(),
                c.getCategory(), c.getStatus())).toList();
    }

    public Class createClassByUser(UndeterminedUser user, CreateClassDTO dto) {
        Class newClass = new Class();
        newClass.setName(dto.name());
        newClass.setCreatedBy(user.email());
        newClass.setCategory(dto.category());
        return classRepository.save(newClass);
    }


    public void editClass(UndeterminedUser user, EditClassDTO dto) {
        Class requestedClass = getClass(user, dto.id());
        String newName = !Objects.isNull(dto.name()) && !dto.name().isBlank() ? dto.name() : requestedClass.getName();
        ECategory newCategory = dto.category() != null ? dto.category() : requestedClass.getCategory();
        requestedClass.setName(newName);
        requestedClass.setCategory(newCategory);
        classRepository.save(requestedClass);
    }

    public void deleteClass(UndeterminedUser user, UUID id) {
        Class requestedClass = getClass(user, id);
        requestedClass.disableClass();
        classRepository.save(requestedClass);
    }

    private Class getClass(UndeterminedUser user, UUID id) {
        Class requestedClass = classRepository.findByIdAndCreatedBy(id, user.email());
        if (requestedClass == null)
            throw new EntityNotFoundException("Class not found.");
        return requestedClass;
    }

    private List<Class> getAllClassesByUndeterminedUser(UndeterminedUser user) {
        return classRepository.findClassesByCreatedBy(user.email());
    }

}
