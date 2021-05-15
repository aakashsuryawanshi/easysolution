package com.apt.wii.repository;

import com.apt.wii.domain.Semester;
import com.apt.wii.domain.Subject;
import com.apt.wii.service.dto.SemesterDTO;
import com.apt.wii.service.dto.SubjectDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Subject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySemester(Semester semesterDTO);
}
