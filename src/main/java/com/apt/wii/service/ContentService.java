package com.apt.wii.service;

import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.ContentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Content}.
 */
public interface ContentService {
    /**
     * Save a content.
     *
     * @param contentDTO the entity to save.
     * @return the persisted entity.
     */
    ContentDTO save(ContentDTO contentDTO);

    /**
     * Partially updates a content.
     *
     * @param contentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContentDTO> partialUpdate(ContentDTO contentDTO);

    /**
     * Get all the contents.
     *
     * @return the list of entities.
     */
    List<ContentDTO> findAll();

    /**
     * Get the "id" content.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentDTO> findOne(Long id);

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<ContentDTO> findByQuestion(Long id);

    /**
     * Delete the "id" content.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
