package com.apt.wii.repository;

import com.apt.wii.domain.Semester;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Semester entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
}
