package com.use3w.grade.projection;

public interface PendingAssessmentProjection {
    String getId();
    String getName();
    java.sql.Date getAssessmentDate();
    Integer getClassesCount();
}
