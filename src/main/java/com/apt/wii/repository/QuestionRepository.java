package com.apt.wii.repository;

import com.apt.wii.domain.Question;
import com.apt.wii.domain.Subject;
import com.apt.wii.domain.TagMetaData;
import com.apt.wii.service.dto.SubjectDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    Page<Question> findBySubject(Subject b, Pageable pageable);

    @Query("select p from Question p left join p.tags q where q.key > ?1 and q.value < ?2")
    List<Question> getQuestionsByTag(String key, String value, Pageable pageable);
}
