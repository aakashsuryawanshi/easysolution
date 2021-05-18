package com.apt.wii.repository;

import com.apt.wii.domain.Content;
import com.apt.wii.domain.Question;
import com.apt.wii.domain.TagMetaData;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TagMetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagMetaDataRepository extends PagingAndSortingRepository<TagMetaData, Long> {
    List<TagMetaData> findByQuestion(Question question);
}
