ALTER TABLE assessments_students
    RENAME COLUMN feedback TO raw_feedback;

ALTER TABLE assessments_students
    ADD COLUMN final_feedback VARCHAR(1500);
