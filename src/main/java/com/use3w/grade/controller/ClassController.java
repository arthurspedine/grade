package com.use3w.grade.controller;

import com.use3w.grade.dto.ClassDetailsDTO;
import com.use3w.grade.dto.CreateClassDTO;
import com.use3w.grade.dto.EditClassDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final UserService userService;

    private final ClassService classService;

    private final StudentService studentService;

    public ClassController(UserService userService, ClassService classService, StudentService studentService) {
        this.userService = userService;
        this.classService = classService;
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<ClassDetailsDTO>> getAllClasses(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(classService.findAllClassesByUndeterminedUser(user));
    }

    @PostMapping
    public ResponseEntity<Void> createClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody @Valid CreateClassDTO dto
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Class newClass = classService.createClassByUser(user, dto);
        studentService.addStudentsToClass(newClass, dto.students());
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> editClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody @Valid EditClassDTO dto
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        classService.editClass(user, dto);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("id") UUID id
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        classService.deleteClass(user, id);
        return ResponseEntity.status(204).build();
    }

}
