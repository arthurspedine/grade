package com.use3w.grade.controller;

import com.use3w.grade.dto.ClassDetailsDTO;
import com.use3w.grade.dto.ClassInfoDTO;
import com.use3w.grade.dto.CreateClassDTO;
import com.use3w.grade.dto.EditClassDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.service.UserService;
import jakarta.validation.Valid;
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
        return ResponseEntity.ok(classService.findAllClassesByUndeterminedUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassInfoDTO> getClassInfo(
            @PathVariable("id") UUID id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        return ResponseEntity.ok(classService.getClassInfoByUndeterminedUserAndId(user, id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createClass(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam("name") String name,
            @RequestParam("category") ECategory category,
            @RequestParam("file") MultipartFile file) {
        CreateClassDTO dto = new CreateClassDTO(name, category, file);
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Class newClass = classService.createClassByUser(user, dto);
        studentService.addStudentsToClass(newClass, dto.file());
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
        EditClassDTO dto = new EditClassDTO(id,name, category);
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Class editedClass = classService.editClass(user, dto);
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
        classService.deleteClass(user, id);
        return ResponseEntity.status(204).build();
    }

}
