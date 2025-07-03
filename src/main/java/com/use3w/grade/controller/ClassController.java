package com.use3w.grade.controller;

import com.use3w.grade.dto.ClassDetailsDTO;
import com.use3w.grade.dto.ClassInfoDTO;
import com.use3w.grade.dto.ClassPerformanceDTO;
import com.use3w.grade.model.Class;
import com.use3w.grade.model.ECategory;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.util.UserAuthentication;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/classes")
public class ClassController {

    private final UserAuthentication userAuthentication;

    private final ClassService classService;

    private final StudentService studentService;

    public ClassController(UserAuthentication userAuthentication, ClassService classService, StudentService studentService) {
        this.userAuthentication = userAuthentication;
        this.classService = classService;
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<ClassDetailsDTO>> getAllClasses() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(classService.findAllClassesByCreatedBy(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassInfoDTO> getClassInfo(
            @PathVariable("id") UUID id) {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(classService.getClassInfoByCreatedByAndId(user, id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<Void> createClass(
            @RequestParam("name") String name,
            @RequestParam("category") ECategory category,
            @RequestParam("file") MultipartFile file) {
        String user = userAuthentication.getCurrentUser();
        Class newClass = classService.createClassByUser(name, user, category);
        studentService.addStudentsToClass(newClass, file);
        return ResponseEntity.status(201).build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editClass(
            @RequestParam("id") UUID id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) ECategory category,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        String user = userAuthentication.getCurrentUser();
        Class editedClass = new Class(id, name, user, category);
        editedClass = classService.editClass(user, editedClass);
        if (file != null)
            studentService.editStudentsFromClass(editedClass, file);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteClass(
            @RequestParam("id") UUID id
    ) {
        String user = userAuthentication.getCurrentUser();
        classService.deleteClass(user, id);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/performance")
    public ResponseEntity<List<ClassPerformanceDTO>> getClassesPerformance() {
        String user = userAuthentication.getCurrentUser();
        return ResponseEntity.ok(classService.getClassesPerformance(user));
    }

}
