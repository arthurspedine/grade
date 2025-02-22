package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "assessments_classes")
public class AssessmentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class assessmentClass;

    public AssessmentClass(Assessment assessment, Class assessmentClass) {
        this.assessment = assessment;
        this.assessmentClass = assessmentClass;
    }

    public AssessmentClass() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentClass that = (AssessmentClass) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public Class getAssessmentClass() {
        return assessmentClass;
    }
}
