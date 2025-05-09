package com.use3w.grade.repository;

import com.use3w.grade.model.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<Class, UUID> {

    List<Class> findClassesByCreatedByAndActiveIsTrue(String createdBy);

    Class findByIdAndCreatedByAndActiveIsTrue(UUID id, String createdBy);

    @Query("SELECT c FROM Class c WHERE c.id IN :ids AND c.active = TRUE AND c.createdBy = :createdBy")
    List<Class> findClassesByIdInAndActiveIsTrueAndCreatedBy(@Param("ids") List<UUID> ids, @Param("createdBy") String createdBy);

    @Query("""
            SELECT COUNT(c)
            FROM Class c
            WHERE c.createdBy = :createdBy
            AND c.active = true
            """)
    Integer countByClassCreatedBy(@Param("createdBy") String createdBy);
}
