package com.use3w.grade.model;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(nullable = false, unique = true)
    @CsvBindByName(column = "rm", required = true)
    private String rm;

    @Column(nullable = false)
    @CsvBindByName(column = "name", required = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "students_classes",
            joinColumns = @JoinColumn(name = "student_rm"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    private Set<Class> classes = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<AssessmentStudent> assessments = new HashSet<>();

    public Student() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(rm, student.rm);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rm);
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
