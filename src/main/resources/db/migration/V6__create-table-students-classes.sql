CREATE TABLE students_classes(
    student_rm VARCHAR(7) NOT NULL,
    class_id BINARY(16) NOT NULL,
    FOREIGN KEY (student_rm) REFERENCES students(rm),
    FOREIGN KEY (class_id) REFERENCES classes(id)
)