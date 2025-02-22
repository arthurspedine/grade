package com.use3w.grade.model;

import jakarta.persistence.*;

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

}
