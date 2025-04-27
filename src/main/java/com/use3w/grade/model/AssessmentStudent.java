package com.use3w.grade.model;

import jakarta.persistence.*;

import java.time.LocalDate;
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

    private LocalDate finishedDate;

    private Double totalScore;

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

    public void setRawFeedback(String rawFeedback) {
        this.rawFeedback = rawFeedback;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }

    public void setFinalFeedback(String finalFeedback) {
        this.finalFeedback = finalFeedback;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public LocalDate getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDate finishedDate) {
        this.finishedDate = finishedDate;
    }
}