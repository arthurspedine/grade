ALTER TABLE assessments
    ADD COLUMN assessment_date DATE;

UPDATE assessments
    SET assessment_date = CURDATE() WHERE assessment_date IS NULL;

ALTER TABLE assessments
    MODIFY COLUMN assessment_date DATE NOT NULL;
