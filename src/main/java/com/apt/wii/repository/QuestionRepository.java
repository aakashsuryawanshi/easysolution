package com.apt.wii.repository;

import com.apt.wii.domain.Question;
import com.apt.wii.domain.Subject;
import com.apt.wii.domain.TagMetaData;
import com.apt.wii.service.dto.SubjectDTO;
import java.util.List;
import java.util.Set;
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
    Page<Question> findBySubject(Subject subject, Pageable pageable);
    Page<Question> findBySubjectAndTitleContainingIgnoreCase(Subject subject, String title, Pageable pageable);

    @Query(
        "SELECT DISTINCT(que) FROM Question que LEFT JOIN que.subject sub LEFT JOIN que.tags t WHERE sub.id = ?1 AND t.key IN ?2 AND t.value IN ?3"
    )
    Page<Question> getQuestionsBySubjectAndTags(Long subjectId, Set<String> keys, List<String> values, Pageable pageable);
}
