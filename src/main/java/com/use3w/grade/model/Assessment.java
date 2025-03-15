package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "assessments_classes",
            joinColumns = @JoinColumn(name = "assessment_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<Class> classes = new HashSet<>();

    @OneToMany(mappedBy = "assessment")
    private Set<AssessmentQuestion> questions = new HashSet<>();

    @Column(nullable = false)
    private String createdBy;

    public Assessment() {
    }

    public Assessment(String name, String createdBy, List<Class> classes) {
        this.name = name;
        this.createdBy = createdBy;
        this.classes.addAll(classes);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Assessment that = (Assessment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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

    public Set<Class> getClasses() {
        return classes;
    }

    public Set<AssessmentQuestion> getQuestions() {
        return questions;
    }
}
