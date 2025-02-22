CREATE TABLE assessments_answers (
    id BINARY(16) NOT NULL PRIMARY KEY,
    assessment_student_id BINARY(16) NOT NULL,
    question_id BINARY(16) NOT NULL,
    score INTEGER NOT NULL,
    FOREIGN KEY (assessment_student_id) REFERENCES assessments_students(id),
    FOREIGN KEY (question_id) REFERENCES assessments_questions(id)
);