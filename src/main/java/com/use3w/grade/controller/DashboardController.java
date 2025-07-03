package com.use3w.grade.controller;

import com.use3w.grade.dto.DashboardStatsDTO;
import com.use3w.grade.service.AssessmentService;
import com.use3w.grade.service.ClassService;
import com.use3w.grade.service.StudentService;
import com.use3w.grade.util.UserAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserAuthentication userAuthentication;
    private final StudentService studentService;
    private final ClassService classService;
    private final AssessmentService assessmentService;

    public DashboardController(UserAuthentication userAuthentication, StudentService studentService, ClassService classService, AssessmentService assessmentService) {
        this.userAuthentication = userAuthentication;
        this.studentService = studentService;
        this.classService = classService;
        this.assessmentService = assessmentService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        String user = userAuthentication.getCurrentUser();
        Integer totalStudents = studentService.countTotalStudents(user);
        Integer totalClasses = classService.countTotalClasses(user);
        Integer totalAssessments = assessmentService.countTotalAssessments(user);
        return ResponseEntity.ok(new DashboardStatsDTO(totalStudents, totalClasses, totalAssessments));
    }
}
