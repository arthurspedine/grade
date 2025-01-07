package com.use3w.grade.service;

import com.use3w.grade.dto.ClassDetails;
import com.use3w.grade.dto.CreateClass;
import com.use3w.grade.dto.EditClass;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<ClassDetails> findAllClassesByUndeterminedUser(UndeterminedUser user) {
        List<Class> classList = getAllClassesByUndeterminedUser(user);
        return classList.stream().map(c -> new ClassDetails(c.getId(), c.getName(),
                c.getCategory(), c.getStatus())).toList();
    }

    public void createClassByUser(UndeterminedUser user, CreateClass createClass) {
        Class newClass = new Class();
        newClass.setName(createClass.name());
        newClass.setCreatedBy(user.email());
        newClass.setCategory(createClass.category());
        classRepository.save(newClass);
    }


    public void editClass(UndeterminedUser user, EditClass editClass) {
        Class requestedClass = getClass(user, editClass.id());
        String newName = !Objects.isNull(editClass.name()) && !editClass.name().isBlank() ? editClass.name() : requestedClass.getName();
        ECategory newCategory = editClass.category() != null ? editClass.category() : requestedClass.getCategory();
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
