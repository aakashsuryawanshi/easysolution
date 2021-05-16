package com.apt.wii.service;

import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.QuestionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Branch}.
 */
public interface BranchService {
    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save.
     * @return the persisted entity.
     */
    BranchDTO save(BranchDTO branchDTO);

    /**
     * Partially updates a branch.
     *
     * @param branchDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BranchDTO> partialUpdate(BranchDTO branchDTO);

    /**
     * Get all the branches.
     *
     * @return the list of entities.
     */
    List<BranchDTO> findAll();

    /**
     * Get the "id" branch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BranchDTO> findOne(Long id);

    /**
     * Get the "id" domain.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    List<BranchDTO> findByDomain(Long id, int page, int size);

    /**
     * Delete the "id" branch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
