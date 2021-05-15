package com.apt.wii.web.rest;

import com.apt.wii.repository.DomainRepository;
import com.apt.wii.service.DomainService;
import com.apt.wii.service.dto.DomainDTO;
import com.apt.wii.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.apt.wii.domain.Domain}.
 */
@RestController
@RequestMapping("/api")
public class DomainResource {

    private final Logger log = LoggerFactory.getLogger(DomainResource.class);

    private static final String ENTITY_NAME = "domain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomainService domainService;

    private final DomainRepository domainRepository;

    public DomainResource(DomainService domainService, DomainRepository domainRepository) {
        this.domainService = domainService;
        this.domainRepository = domainRepository;
    }

    /**
     * {@code POST  /domains} : Create a new domain.
     *
     * @param domainDTO the domainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domainDTO, or with status {@code 400 (Bad Request)} if the domain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secure/domains")
    public ResponseEntity<DomainDTO> createDomain(@RequestBody DomainDTO domainDTO) throws URISyntaxException {
        log.debug("REST request to save Domain : {}", domainDTO);
        if (domainDTO.getId() != null) {
            throw new BadRequestAlertException("A new domain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DomainDTO result = domainService.save(domainDTO);
        return ResponseEntity
            .created(new URI("/api/domains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domains/:id} : Updates an existing domain.
     *
     * @param id the id of the domainDTO to save.
     * @param domainDTO the domainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domainDTO,
     * or with status {@code 400 (Bad Request)} if the domainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secure/domains/{id}")
    public ResponseEntity<DomainDTO> updateDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomainDTO domainDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Domain : {}, {}", id, domainDTO);
        if (domainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DomainDTO result = domainService.save(domainDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, domainDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /domains/:id} : Partial updates given fields of an existing domain, field will ignore if it is null
     *
     * @param id the id of the domainDTO to save.
     * @param domainDTO the domainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domainDTO,
     * or with status {@code 400 (Bad Request)} if the domainDTO is not valid,
     * or with status {@code 404 (Not Found)} if the domainDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the domainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/secure/domains/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DomainDTO> partialUpdateDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomainDTO domainDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Domain partially : {}, {}", id, domainDTO);
        if (domainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domainDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DomainDTO> result = domainService.partialUpdate(domainDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, domainDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /domains} : get all the domains.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domains in body.
     */
    @GetMapping("/domains")
    public List<DomainDTO> getAllDomains() {
        log.debug("REST request to get all Domains");
        return domainService.findAll();
    }

    /**
     * {@code GET  /domains/:id} : get the "id" domain.
     *
     * @param id the id of the domainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/domains/{id}")
    public ResponseEntity<DomainDTO> getDomain(@PathVariable Long id) {
        log.debug("REST request to get Domain : {}", id);
        Optional<DomainDTO> domainDTO = domainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domainDTO);
    }

    /**
     * {@code DELETE  /domains/:id} : delete the "id" domain.
     *
     * @param id the id of the domainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secure/domains/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        log.debug("REST request to delete Domain : {}", id);
        domainService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
