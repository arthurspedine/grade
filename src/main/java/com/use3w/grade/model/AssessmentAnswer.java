package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Table(name = "assessments_answers")
@Entity
public class AssessmentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private AssessmentStudent assessmentStudent;

    @ManyToOne
    private AssessmentQuestion question;

    private Double score;

    public AssessmentAnswer() {
    }

    public UUID getId() {
        return id;
    }

    public AssessmentQuestion getQuestion() {
        return question;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentAnswer that = (AssessmentAnswer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
