package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "assessments_questions")
public class AssessmentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    private Assessment assessment;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Integer questionNumber;

    @OneToMany
    private Set<AssessmentAnswer> answers;

    public AssessmentQuestion() {
    }

    public AssessmentQuestion(Assessment assessment, String name, Double score, Integer questionNumber) {
        this.assessment = assessment;
        this.name = name;
        this.score = score;
        setQuestionNumber(questionNumber);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setQuestionNumber(Integer questionNumber) {
        if (questionNumber <= 0)
            throw new IllegalArgumentException("O número da questão não pode ser menor que 0");
        this.questionNumber = questionNumber;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentQuestion that = (AssessmentQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
