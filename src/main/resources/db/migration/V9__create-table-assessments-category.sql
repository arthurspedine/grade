CREATE TABLE assessments_category(
    id BINARY(16) NOT NULL PRIMARY KEY,
    assessment_id BINARY(16) NOT NULL,
    name VARCHAR(200) NOT NULL,
    score INTEGER NOT NULL,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id)
);