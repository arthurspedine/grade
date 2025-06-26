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

    @Column(name = "assessment_student_id")
    private UUID assessmentStudentId;

    @Column(name = "question_id")
    private UUID questionId;

    private Double score;

    public AssessmentAnswer() {
    }

    public AssessmentAnswer(UUID questionId, Double score) {
        this.questionId = questionId;
        this.score = score;
    }

    public UUID getId() {
        return id;
    }

    public void setAssessmentStudentId(UUID assessmentStudentId) {
        this.assessmentStudentId = assessmentStudentId;
    }

    public UUID getQuestionId() {
        return questionId;
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
