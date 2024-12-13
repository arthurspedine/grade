package com.use3w.grade.controller;

import com.use3w.grade.dto.ClassDetails;
import com.use3w.grade.dto.CreateClass;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

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
}
