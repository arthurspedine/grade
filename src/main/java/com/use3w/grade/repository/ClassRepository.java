package com.use3w.grade.repository;

import com.use3w.grade.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<Class, UUID> {

    List<Class> findClassesByCreatedByAndActiveIsTrue(String createdBy);

    Class findByIdAndCreatedByAndActiveIsTrue(UUID id, String createdBy);

    List<Class> findClassesByIdInAndActiveIsTrueAndCreatedBy(List<UUID> ids, String createdBy);
}
