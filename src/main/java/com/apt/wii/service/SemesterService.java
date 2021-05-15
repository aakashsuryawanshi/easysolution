package com.apt.wii.service;

import com.apt.wii.service.dto.SemesterDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Semester}.
 */
public interface SemesterService {
    /**
     * Save a semester.
     *
     * @param semesterDTO the entity to save.
     * @return the persisted entity.
     */
    SemesterDTO save(SemesterDTO semesterDTO);

    /**
     * Partially updates a semester.
     *
     * @param semesterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SemesterDTO> partialUpdate(SemesterDTO semesterDTO);

    /**
     * Get all the semesters.
     *
     * @return the list of entities.
     */
    List<SemesterDTO> findAll();

    /**
     * Get the "id" semester.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SemesterDTO> findOne(Long id);

    /**
     * Get the "id" semester.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<SemesterDTO> findByBranch(Long id);

    /**
     * Delete the "id" semester.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
