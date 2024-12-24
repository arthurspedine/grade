package com.use3w.grade.controller;

import com.use3w.grade.dto.ClassDetails;
import com.use3w.grade.dto.CreateClass;
import com.use3w.grade.dto.EditClass;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.UserService;
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

    public ClassController(UserService userService, ClassService classService) {
        this.userService = userService;
        this.classService = classService;
    }

    @GetMapping
    public ResponseEntity<List<ClassDetails>> getAllClasses(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(classService.findAllClassesByUndeterminedUser(user));
    }

    @PostMapping
    public ResponseEntity<Void> createClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody CreateClass createClass
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        classService.createClassByUser(user, createClass);
        return ResponseEntity.status(201).build();
    }

    @PutMapping
    public ResponseEntity<Void> editClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody EditClass editClass
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        classService.editClass(user, editClass);
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
