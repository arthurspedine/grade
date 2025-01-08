package com.use3w.grade.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(nullable = false, unique = true)
    private String rm;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "students_classes",
            joinColumns = @JoinColumn(name = "student_rm"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<Class> classes = new HashSet<>();

    public Student() {
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
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
}
