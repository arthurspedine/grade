package com.use3w.grade.controller;

import com.use3w.grade.dto.DashboardStatsDTO;
import com.use3w.grade.model.UndeterminedUser;
import com.use3w.grade.service.AssessmentService;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final StudentService studentService;
    private final ClassService classService;
    private final AssessmentService assessmentService;

    public DashboardController(UserService userService, StudentService studentService, ClassService classService, AssessmentService assessmentService) {
        this.userService = userService;
        this.studentService = studentService;
        this.classService = classService;
        this.assessmentService = assessmentService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        UndeterminedUser user = userService.fetchUndeterminedUserByHeader(authHeader);
        Integer totalStudents = studentService.countTotalStudents(user.email());
        Integer totalClasses = classService.countTotalClasses(user.email());
        Integer totalAssessments = assessmentService.countTotalAssessments(user.email());
        return ResponseEntity.ok(new DashboardStatsDTO(totalStudents, totalClasses, totalAssessments));
    }
}
