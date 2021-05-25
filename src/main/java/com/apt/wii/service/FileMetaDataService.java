package com.apt.wii.service;

import com.apt.wii.service.dto.FileMetaDataDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.FileMetaData}.
 */
public interface FileMetaDataService {
    /**
     * Save a fileMetaData.
     *
     * @param fileMetaDataDTO the entity to save.
     * @return the persisted entity.
     */
    FileMetaDataDTO save(FileMetaDataDTO fileMetaDataDTO);

    /**
     * Partially updates a fileMetaData.
     *
     * @param fileMetaDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileMetaDataDTO> partialUpdate(FileMetaDataDTO fileMetaDataDTO);

    /**
     * Get all the fileMetaData.
     *
     * @return the list of entities.
     */
    List<FileMetaDataDTO> findAll();

    /**
     * Get all the fileMetaData by fileDetail Id.
     *
     * @return the list of entities.
     */
    List<FileMetaDataDTO> findByFileDetail(Long fileDetails);

    /**
     * Get the "id" fileMetaData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileMetaDataDTO> findOne(Long id);

    /**
     * Delete the "id" fileMetaData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
