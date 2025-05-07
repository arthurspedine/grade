package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "classes")
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECategory category = ECategory.BACHELORS;

    @ManyToMany(mappedBy = "classes")
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "classes")
    private Set<Assessment> assessments = new HashSet<>();

    public Class() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return Objects.equals(id, aClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void disableClass() {
        this.active = false;
    }

    public Boolean getStatus() {
        return this.active;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return active;
    }

    public ECategory getCategory() {
        return category;
    }

    public void setCategory(ECategory category) {
        this.category = category;
    }

    public Set<Student> getStudents() {
        return students;
    }
}
