ALTER TABLE assessments_students
ADD COLUMN class_id BINARY(16) NULL AFTER assessment_id;

UPDATE assessments_students ast
SET class_id = (
    SELECT ac.class_id
    FROM assessments_classes ac
    INNER JOIN students_classes sc ON sc.class_id = ac.class_id
    INNER JOIN students s ON s.rm = sc.student_rm
    WHERE ac.assessment_id = ast.assessment_id
    AND s.rm = ast.student_rm
    LIMIT 1
)
WHERE ast.class_id IS NULL;
ALTER TABLE assessments_students
MODIFY COLUMN class_id BINARY(16) NOT NULL;

ALTER TABLE assessments_students
ADD CONSTRAINT fk_assessments_students_class_id
FOREIGN KEY (class_id) REFERENCES classes (id);