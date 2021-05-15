package com.apt.wii.service;

import com.apt.wii.service.dto.SemesterDTO;
import com.apt.wii.service.dto.SubjectDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Subject}.
 */
public interface SubjectService {
    /**
     * Save a subject.
     *
     * @param subjectDTO the entity to save.
     * @return the persisted entity.
     */
    SubjectDTO save(SubjectDTO subjectDTO);

    /**
     * Partially updates a subject.
     *
     * @param subjectDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubjectDTO> partialUpdate(SubjectDTO subjectDTO);

    /**
     * Get all the subjects.
     *
     * @return the list of entities.
     */
    List<SubjectDTO> findAll();

    /**
     * Get the "id" subject.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubjectDTO> findOne(Long id);

    /**
     * Get the "id" semester.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<SubjectDTO> findBySemester(Long id);

    /**
     * Delete the "id" subject.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
