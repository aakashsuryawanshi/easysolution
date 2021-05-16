package com.apt.wii.service;

import com.apt.wii.domain.TagMetaData;
import com.apt.wii.service.dto.QuestionDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.dto.TagMetaDataDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     * Partially updates a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO);

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    List<QuestionDTO> findAll();

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuestionDTO> findOne(Long id);

    /**
     * Get the "id" subject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<QuestionDTO> findBySubject(Long subjectId, int page, int size);

    /**
     * Get the "id" subject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<QuestionDTO> findByTag(List<TagMetaDataDTO> tags, String ops, int page, int size);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
