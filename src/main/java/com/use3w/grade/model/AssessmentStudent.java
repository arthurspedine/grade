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
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    private String rawFeedback;

    private String finalFeedback;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean finished;

    private Double totalScore;

    @OneToMany
    private Set<AssessmentAnswer> answers;

    public AssessmentStudent() {
    }

    public AssessmentStudent(Student student, Assessment assessment) {
        this.student = student;
        this.assessment = assessment;
        this.finished = false;
        this.totalScore = 0d;
    }

    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public String getRawFeedback() {
        return rawFeedback;
    }

    public String getFinalFeedback() {
        return finalFeedback;
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