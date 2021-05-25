package com.apt.wii.web.rest;

import com.apt.wii.repository.FileMetaDataRepository;
import com.apt.wii.service.FileMetaDataService;
import com.apt.wii.service.dto.FileMetaDataDTO;
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
 * REST controller for managing {@link com.apt.wii.domain.FileMetaData}.
 */
@RestController
@RequestMapping("/api")
public class FileMetaDataResource {

    private final Logger log = LoggerFactory.getLogger(FileMetaDataResource.class);

    private static final String ENTITY_NAME = "fileMetaData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileMetaDataService fileMetaDataService;

    private final FileMetaDataRepository fileMetaDataRepository;

    public FileMetaDataResource(FileMetaDataService fileMetaDataService, FileMetaDataRepository fileMetaDataRepository) {
        this.fileMetaDataService = fileMetaDataService;
        this.fileMetaDataRepository = fileMetaDataRepository;
    }

    /**
     * {@code POST  /file-meta-data} : Create a new fileMetaData.
     *
     * @param fileMetaDataDTO the fileMetaDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileMetaDataDTO, or with status {@code 400 (Bad Request)} if the fileMetaData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secure/file-meta-data")
    public ResponseEntity<FileMetaDataDTO> createFileMetaData(@RequestBody FileMetaDataDTO fileMetaDataDTO) throws URISyntaxException {
        log.debug("REST request to save FileMetaData : {}", fileMetaDataDTO);
        if (fileMetaDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileMetaData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileMetaDataDTO result = fileMetaDataService.save(fileMetaDataDTO);
        return ResponseEntity
            .created(new URI("/api/file-meta-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-meta-data/:id} : Updates an existing fileMetaData.
     *
     * @param id the id of the fileMetaDataDTO to save.
     * @param fileMetaDataDTO the fileMetaDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileMetaDataDTO,
     * or with status {@code 400 (Bad Request)} if the fileMetaDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileMetaDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secure/file-meta-data/{id}")
    public ResponseEntity<FileMetaDataDTO> updateFileMetaData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileMetaDataDTO fileMetaDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileMetaData : {}, {}", id, fileMetaDataDTO);
        if (fileMetaDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileMetaDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileMetaDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileMetaDataDTO result = fileMetaDataService.save(fileMetaDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileMetaDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-meta-data/:id} : Partial updates given fields of an existing fileMetaData, field will ignore if it is null
     *
     * @param id the id of the fileMetaDataDTO to save.
     * @param fileMetaDataDTO the fileMetaDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileMetaDataDTO,
     * or with status {@code 400 (Bad Request)} if the fileMetaDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileMetaDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileMetaDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/secure/file-meta-data/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FileMetaDataDTO> partialUpdateFileMetaData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileMetaDataDTO fileMetaDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileMetaData partially : {}, {}", id, fileMetaDataDTO);
        if (fileMetaDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileMetaDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileMetaDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileMetaDataDTO> result = fileMetaDataService.partialUpdate(fileMetaDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileMetaDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /file-meta-data} : get all the fileMetaData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileMetaData in body.
     */
    @GetMapping("/file-meta-data")
    public List<FileMetaDataDTO> getAllFileMetaData() {
        log.debug("REST request to get all FileMetaData");
        return fileMetaDataService.findAll();
    }

    /**
     * {@code GET  /file-meta-data/:id} : get the "id" fileMetaData.
     *
     * @param id the id of the fileMetaDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileMetaDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-meta-data/{id}")
    public ResponseEntity<FileMetaDataDTO> getFileMetaData(@PathVariable Long id) {
        log.debug("REST request to get FileMetaData : {}", id);
        Optional<FileMetaDataDTO> fileMetaDataDTO = fileMetaDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileMetaDataDTO);
    }

    /**
     * {@code DELETE  /file-meta-data/:id} : delete the "id" fileMetaData.
     *
     * @param id the id of the fileMetaDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secure/file-meta-data/{id}")
    public ResponseEntity<Void> deleteFileMetaData(@PathVariable Long id) {
        log.debug("REST request to delete FileMetaData : {}", id);
        fileMetaDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
