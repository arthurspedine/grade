CREATE TABLE assessments_students (
    id            BINARY(16) NOT NULL PRIMARY KEY,
    assessment_class_id BINARY(16) NOT NULL,
    student_rm    VARCHAR(7) NOT NULL,
    feedback      VARCHAR(1500),
    finished      TINYINT(1) NOT NULL DEFAULT 0,
    total_score   INTEGER,
    FOREIGN KEY (assessment_class_id) REFERENCES assessments_classes (id),
    FOREIGN KEY (student_rm) REFERENCES students (rm)
);