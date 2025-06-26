package com.use3w.grade.projection;

import org.springframework.beans.factory.annotation.Value;

public interface StudentClassProjection {
    @Value("#{'RM' + target.rm + ' (' + target.name + ')'}")
    String getStudentClass();
}
