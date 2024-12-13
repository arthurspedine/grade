package com.use3w.grade.model;

import jakarta.persistence.*;

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
    private String created_by;
}
