package com.use3w.grade.service;

import com.use3w.grade.dto.ClassDetails;
import com.use3w.grade.dto.CreateClass;
import com.use3w.grade.dto.EditClass;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.repository.ClassRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassService {

    @Autowired
    private ClassRepository repository;
    @Autowired
    private ClassRepository classRepository;

    public List<ClassDetails> findAllClassesByUndeterminedUser(UndeterminedUser user) {
        List<Class> classList = getAllClassesByUndeterminedUser(user);
        return classList.stream().map(c -> new ClassDetails(c.getId(), c.getName(), c.getStatus())).toList();
    }

    public void createClassByUser(UndeterminedUser user, CreateClass createClass) {
        Class newClass = new Class();
        newClass.setName(createClass.name());
        newClass.setCreatedBy(user.email());
        repository.save(newClass);
    }


    public void editClass(UndeterminedUser user, EditClass editClass) {
        Class requestedClass = getClass(user, editClass.id());
        requestedClass.setName(editClass.name());
        repository.save(requestedClass);
    }

    public void deleteClass(UndeterminedUser user, UUID id) {
        Class requestedClass = getClass(user, id);
        requestedClass.disableClass();
        repository.save(requestedClass);
    }

    private Class getClass(UndeterminedUser user, UUID id) {
        Class requestedClass = repository.findByIdAndCreatedBy(id, user.email());
        if (requestedClass == null)
            throw new EntityNotFoundException("Class not found.");
        return requestedClass;
    }

    private List<Class> getAllClassesByUndeterminedUser(UndeterminedUser user) {
        return repository.findClassesByCreatedBy(user.email());
    }

}
