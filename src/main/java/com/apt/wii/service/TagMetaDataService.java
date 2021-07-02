package com.apt.wii.service;

import com.apt.wii.service.dto.TagMetaDataDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.TagMetaData}.
 */
public interface TagMetaDataService {
    /**
     * Save a tagMetaData.
     *
     * @param tagMetaDataDTO the entity to save.
     * @return the persisted entity.
     */
    TagMetaDataDTO save(TagMetaDataDTO tagMetaDataDTO);

    /**
     * Partially updates a tagMetaData.
     *
     * @param tagMetaDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagMetaDataDTO> partialUpdate(TagMetaDataDTO tagMetaDataDTO);

    /**
     * Get all the tagMetaData.
     *
     * @return the list of entities.
     */
    List<TagMetaDataDTO> findAll();

    /**
     * Get the "id" tagMetaData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagMetaDataDTO> findOne(Long id);

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @param pageSize
     * @param pageNo
     * @return the entity.
     */
    List<TagMetaDataDTO> findByQuestion(Long id);

    /**
     * Delete the "id" tagMetaData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Map<String, List<String>> findAllUniqueTags();
}
