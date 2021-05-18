package com.apt.wii.web.rest;

import com.apt.wii.repository.SemesterRepository;
import com.apt.wii.service.SemesterService;
import com.apt.wii.service.dto.SemesterDTO;
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
 * REST controller for managing {@link com.apt.wii.domain.Semester}.
 */
@RestController
@RequestMapping("/api")
public class SemesterResource {

    private final Logger log = LoggerFactory.getLogger(SemesterResource.class);

    private static final String ENTITY_NAME = "semester";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemesterService semesterService;

    private final SemesterRepository semesterRepository;

    public SemesterResource(SemesterService semesterService, SemesterRepository semesterRepository) {
        this.semesterService = semesterService;
        this.semesterRepository = semesterRepository;
    }

    /**
     * {@code POST  /semesters} : Create a new semester.
     *
     * @param semesterDTO the semesterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semesterDTO, or with status {@code 400 (Bad Request)} if the semester has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secure/semesters")
    public ResponseEntity<SemesterDTO> createSemester(@RequestBody SemesterDTO semesterDTO) throws URISyntaxException {
        log.debug("REST request to save Semester : {}", semesterDTO);
        if (semesterDTO.getId() != null) {
            throw new BadRequestAlertException("A new semester cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SemesterDTO result = semesterService.save(semesterDTO);
        return ResponseEntity
            .created(new URI("/api/secure/semesters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /semesters/:id} : Updates an existing semester.
     *
     * @param id the id of the semesterDTO to save.
     * @param semesterDTO the semesterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semesterDTO,
     * or with status {@code 400 (Bad Request)} if the semesterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semesterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secure/semesters/{id}")
    public ResponseEntity<SemesterDTO> updateSemester(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SemesterDTO semesterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Semester : {}, {}", id, semesterDTO);
        if (semesterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semesterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semesterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SemesterDTO result = semesterService.save(semesterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, semesterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /semesters/:id} : Partial updates given fields of an existing semester, field will ignore if it is null
     *
     * @param id the id of the semesterDTO to save.
     * @param semesterDTO the semesterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semesterDTO,
     * or with status {@code 400 (Bad Request)} if the semesterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the semesterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the semesterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/secure/semesters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SemesterDTO> partialUpdateSemester(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SemesterDTO semesterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Semester partially : {}, {}", id, semesterDTO);
        if (semesterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semesterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semesterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SemesterDTO> result = semesterService.partialUpdate(semesterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, semesterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /semesters} : get all the semesters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semesters in body.
     */
    @GetMapping("/semesters")
    public List<SemesterDTO> getAllSemesters() {
        log.debug("REST request to get all Semesters");
        return semesterService.findAll();
    }

    /**
     * {@code GET  /semesters/:id} : get the "id" semester.
     *
     * @param id the id of the semesterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semesterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/semesters/{id}")
    public ResponseEntity<SemesterDTO> getSemester(@PathVariable Long id) {
        log.debug("REST request to get Semester : {}", id);
        Optional<SemesterDTO> semesterDTO = semesterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semesterDTO);
    }

    /**
     * {@code GET  /semesters/:id} : get the "id" semester.
     *
     * @param id the id of the semesterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semesterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/branch/{id}/semesters")
    public List<SemesterDTO> getSemesterByBranch(@PathVariable Long id) {
        log.debug("REST request to get Semester : {}", id);
        List<SemesterDTO> semesterDTOs = semesterService.findByBranch(id);
        return semesterDTOs;
    }

    /**
     * {@code DELETE  /semesters/:id} : delete the "id" semester.
     *
     * @param id the id of the semesterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secure/semesters/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        log.debug("REST request to delete Semester : {}", id);
        semesterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
