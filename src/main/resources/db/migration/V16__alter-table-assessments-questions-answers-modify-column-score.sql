ALTER TABLE assessments_questions
    MODIFY COLUMN score DECIMAL(5, 2);

ALTER TABLE assessments_answers
    MODIFY COLUMN score DECIMAL(5, 2);
