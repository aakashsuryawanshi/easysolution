package com.apt.wii.web.rest;

import com.apt.wii.repository.FileDetailsRepository;
import com.apt.wii.service.FileDetailsService;
import com.apt.wii.service.dto.FileDetailsDTO;
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
 * REST controller for managing {@link com.apt.wii.domain.FileDetails}.
 */
@RestController
@RequestMapping("/api")
public class FileDetailsResource {

    private final Logger log = LoggerFactory.getLogger(FileDetailsResource.class);

    private static final String ENTITY_NAME = "fileDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileDetailsService fileDetailsService;

    private final FileDetailsRepository fileDetailsRepository;

    public FileDetailsResource(FileDetailsService fileDetailsService, FileDetailsRepository fileDetailsRepository) {
        this.fileDetailsService = fileDetailsService;
        this.fileDetailsRepository = fileDetailsRepository;
    }

    /**
     * {@code POST  /file-details} : Create a new fileDetails.
     *
     * @param fileDetailsDTO the fileDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDetailsDTO, or with status {@code 400 (Bad Request)} if the fileDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secure/file-details")
    public ResponseEntity<FileDetailsDTO> createFileDetails(@RequestBody FileDetailsDTO fileDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save FileDetails : {}", fileDetailsDTO);
        if (fileDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDetailsDTO result = fileDetailsService.save(fileDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/file-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-details/:id} : Updates an existing fileDetails.
     *
     * @param id the id of the fileDetailsDTO to save.
     * @param fileDetailsDTO the fileDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the fileDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secure/file-details/{id}")
    public ResponseEntity<FileDetailsDTO> updateFileDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileDetailsDTO fileDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileDetails : {}, {}", id, fileDetailsDTO);
        if (fileDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileDetailsDTO result = fileDetailsService.save(fileDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-details/:id} : Partial updates given fields of an existing fileDetails, field will ignore if it is null
     *
     * @param id the id of the fileDetailsDTO to save.
     * @param fileDetailsDTO the fileDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the fileDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/secure/file-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FileDetailsDTO> partialUpdateFileDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileDetailsDTO fileDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileDetails partially : {}, {}", id, fileDetailsDTO);
        if (fileDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileDetailsDTO> result = fileDetailsService.partialUpdate(fileDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /file-details} : get all the fileDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileDetails in body.
     */
    @GetMapping("/file-details")
    public List<FileDetailsDTO> getAllFileDetails() {
        log.debug("REST request to get all FileDetails");
        return fileDetailsService.findAll();
    }

    /**
     * {@code GET  /file-details/:id} : get the "id" fileDetails.
     *
     * @param id the id of the fileDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-details/{id}")
    public ResponseEntity<FileDetailsDTO> getFileDetails(@PathVariable Long id) {
        log.debug("REST request to get FileDetails : {}", id);
        Optional<FileDetailsDTO> fileDetailsDTO = fileDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDetailsDTO);
    }

    /**
     * {@code DELETE  /file-details/:id} : delete the "id" fileDetails.
     *
     * @param id the id of the fileDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secure/file-details/{id}")
    public ResponseEntity<Void> deleteFileDetails(@PathVariable Long id) {
        log.debug("REST request to delete FileDetails : {}", id);
        fileDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
