package com.apt.wii.web.rest;

import com.apt.wii.domain.TagMetaData;
import com.apt.wii.repository.TagMetaDataRepository;
import com.apt.wii.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.apt.wii.domain.TagMetaData}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TagMetaDataResource {

    private final Logger log = LoggerFactory.getLogger(TagMetaDataResource.class);

    private static final String ENTITY_NAME = "wiiTagMetaData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagMetaDataRepository tagMetaDataRepository;

    public TagMetaDataResource(TagMetaDataRepository tagMetaDataRepository) {
        this.tagMetaDataRepository = tagMetaDataRepository;
    }

    /**
     * {@code POST  /tag-meta-data} : Create a new tagMetaData.
     *
     * @param tagMetaData the tagMetaData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagMetaData, or with status {@code 400 (Bad Request)} if the tagMetaData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tag-meta-data")
    public ResponseEntity<TagMetaData> createTagMetaData(@RequestBody TagMetaData tagMetaData) throws URISyntaxException {
        log.debug("REST request to save TagMetaData : {}", tagMetaData);
        if (tagMetaData.getId() != null) {
            throw new BadRequestAlertException("A new tagMetaData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagMetaData result = tagMetaDataRepository.save(tagMetaData);
        return ResponseEntity.created(new URI("/api/tag-meta-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tag-meta-data} : Updates an existing tagMetaData.
     *
     * @param tagMetaData the tagMetaData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagMetaData,
     * or with status {@code 400 (Bad Request)} if the tagMetaData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagMetaData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tag-meta-data")
    public ResponseEntity<TagMetaData> updateTagMetaData(@RequestBody TagMetaData tagMetaData) throws URISyntaxException {
        log.debug("REST request to update TagMetaData : {}", tagMetaData);
        if (tagMetaData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TagMetaData result = tagMetaDataRepository.save(tagMetaData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tagMetaData.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tag-meta-data} : get all the tagMetaData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagMetaData in body.
     */
    @GetMapping("/tag-meta-data")
    public List<TagMetaData> getAllTagMetaData() {
        log.debug("REST request to get all TagMetaData");
        return tagMetaDataRepository.findAll();
    }

    /**
     * {@code GET  /tag-meta-data/:id} : get the "id" tagMetaData.
     *
     * @param id the id of the tagMetaData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagMetaData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tag-meta-data/{id}")
    public ResponseEntity<TagMetaData> getTagMetaData(@PathVariable Long id) {
        log.debug("REST request to get TagMetaData : {}", id);
        Optional<TagMetaData> tagMetaData = tagMetaDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tagMetaData);
    }

    /**
     * {@code DELETE  /tag-meta-data/:id} : delete the "id" tagMetaData.
     *
     * @param id the id of the tagMetaData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tag-meta-data/{id}")
    public ResponseEntity<Void> deleteTagMetaData(@PathVariable Long id) {
        log.debug("REST request to delete TagMetaData : {}", id);
        tagMetaDataRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
