package com.use3w.grade.controller;

import com.use3w.grade.dto.*;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok(classService.findAllClassesByUndeterminedUser(user.email()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassInfoDTO> getClassInfo(
            @PathVariable("id") UUID id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(classService.getClassInfoByUndeterminedUserAndId(user.email(), id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> createClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("name") String name,
            @RequestParam("category") ECategory category,
            @RequestParam("file") MultipartFile file) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Class newClass = classService.createClassByUser(name, user.email(), category);
        studentService.addStudentsToClass(newClass, file);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("id") UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) ECategory category,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Class editedClass = new Class(id, name, user.email(), category);
        editedClass = classService.editClass(user.email(), editedClass);
        if (file != null)
            studentService.editStudentsFromClass(editedClass, file);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("id") UUID id
    ) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        classService.deleteClass(user.email(), id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/performance")
    public ResponseEntity<List<ClassPerformanceDTO>> getClassesPerformance(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(classService.getClassesPerformance(user.email()));
    }

}
