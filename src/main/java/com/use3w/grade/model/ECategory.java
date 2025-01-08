package com.use3w.grade.model;

public enum ECategory {
    BACHELORS("Graduação"),
    POSTGRADUATE("Pós-Graduação"),
    MBA("MBA");

    private final String description;


    ECategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
