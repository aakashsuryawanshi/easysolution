package com.apt.wii.repository;

import com.apt.wii.domain.Content;
import com.apt.wii.domain.Question;
import com.apt.wii.domain.Subject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Content entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentRepository extends PagingAndSortingRepository<Content, Long> {
    List<Content> findByQuestion(Question question);
}
