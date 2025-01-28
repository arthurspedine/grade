package com.use3w.grade.model;

import jakarta.persistence.*;

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
    private Integer score;

    @Column(nullable = false)
    private Integer questionNumber;

    public AssessmentQuestion() {
    }

    public AssessmentQuestion(Assessment assessment, String name, Integer score, Integer questionNumber) {
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

    public Integer getScore() {
        return score;
    }

    public void setQuestionNumber(Integer questionNumber) {
        if (questionNumber <= 0)
            throw new IllegalArgumentException("O número da questão não pode ser menor que 0");
        this.questionNumber = questionNumber;
    }
}
