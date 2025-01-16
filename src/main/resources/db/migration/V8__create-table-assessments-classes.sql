CREATE TABLE assessments_classes(
    assessment_id BINARY(16) NOT NULL,
    class_id BINARY(16) NOT NULL,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id),
    FOREIGN KEY (class_id) REFERENCES classes(id)
);