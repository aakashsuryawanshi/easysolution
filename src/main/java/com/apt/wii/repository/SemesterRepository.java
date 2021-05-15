package com.apt.wii.repository;

import com.apt.wii.domain.Branch;
import com.apt.wii.domain.Semester;
import com.apt.wii.service.dto.BranchDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Semester entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    List<Semester> findByBranch(Branch b);
}
