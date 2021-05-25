package com.apt.wii.service;

import com.apt.wii.service.dto.FileDetailsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.FileDetails}.
 */
public interface FileDetailsService {
    /**
     * Save a fileDetails.
     *
     * @param fileDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    FileDetailsDTO save(FileDetailsDTO fileDetailsDTO);

    /**
     * Partially updates a fileDetails.
     *
     * @param fileDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDetailsDTO> partialUpdate(FileDetailsDTO fileDetailsDTO);

    /**
     * Get all the fileDetails.
     *
     * @return the list of entities.
     */
    List<FileDetailsDTO> findAll();

    /**
     * Get the "id" fileDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" fileDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
