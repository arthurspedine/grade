package com.use3w.grade.service;

import com.use3w.grade.dto.ClassDetails;
import com.use3w.grade.dto.CreateClass;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassRepository repository;

    public List<ClassDetails> findAllClassesByUndeterminedUser(UndeterminedUser user) {
        List<Class> classList = getAllClassesByUndeterminedUser(user);
        return classList.stream().map(c -> new ClassDetails(c.getId(), c.getName())).toList();
    }

    public void createClassByUser(UndeterminedUser user, CreateClass createClass) {
        Class newClass = new Class();
        newClass.setName(createClass.name());
        newClass.setCreatedBy(user.email());
        repository.save(newClass);
    }

    private List<Class> getAllClassesByUndeterminedUser(UndeterminedUser user) {
        return repository.findClassesByCreatedBy(user.email());
    }
}
