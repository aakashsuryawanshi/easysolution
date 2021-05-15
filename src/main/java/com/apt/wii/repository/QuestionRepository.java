package com.apt.wii.repository;

import com.apt.wii.domain.Question;
import com.apt.wii.domain.Subject;
import com.apt.wii.service.dto.SubjectDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySubject(Subject b);
}
