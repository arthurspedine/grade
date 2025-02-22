package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "assessments_students")
public class AssessmentStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assessment_class_id")
    private AssessmentClass assessment;

    private String feedback;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean finished;

    private Double totalScore;

    @OneToMany
    private Set<AssessmentAnswer> answers;

    public AssessmentStudent(Student student, AssessmentClass assessment) {
        this.student = student;
        this.assessment = assessment;
        this.finished = false;
    }

    public AssessmentStudent() {
    }

    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public AssessmentClass getAssessment() {
        return assessment;
    }

    public String getFeedback() {
        return feedback;
    }

    public Boolean getFinished() {
        return finished;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public Set<AssessmentAnswer> getAnswers() {
        return answers;
    }
}
