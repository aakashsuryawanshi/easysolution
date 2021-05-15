package com.apt.wii.service;

import com.apt.wii.service.dto.DomainDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apt.wii.domain.Domain}.
 */
public interface DomainService {
    /**
     * Save a domain.
     *
     * @param domainDTO the entity to save.
     * @return the persisted entity.
     */
    DomainDTO save(DomainDTO domainDTO);

    /**
     * Partially updates a domain.
     *
     * @param domainDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DomainDTO> partialUpdate(DomainDTO domainDTO);

    /**
     * Get all the domains.
     *
     * @return the list of entities.
     */
    List<DomainDTO> findAll();

    /**
     * Get the "id" domain.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DomainDTO> findOne(Long id);

    /**
     * Delete the "id" domain.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
