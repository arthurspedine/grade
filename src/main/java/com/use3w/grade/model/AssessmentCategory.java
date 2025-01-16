package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "assessments_category")
public class AssessmentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Assessment assessment;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer score;

    public AssessmentCategory() {
    }

    public AssessmentCategory(Assessment assessment, String name, Integer score) {
        this.assessment = assessment;
        this.name = name;
        this.score = score;
    }

    public UUID getId() {
        return id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
