package com.apt.wii.web.rest;

import com.apt.wii.domain.Content;
import com.apt.wii.repository.ContentRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.apt.wii.domain.Content}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContentResource {

    private final Logger log = LoggerFactory.getLogger(ContentResource.class);

    private static final String ENTITY_NAME = "content";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentRepository contentRepository;

    public ContentResource(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * {@code POST  /contents} : Create a new content.
     *
     * @param content the content to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new content, or with status {@code 400 (Bad Request)} if the content has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secure/contents")
    public ResponseEntity<Content> createContent(@RequestBody Content content) throws URISyntaxException {
        log.debug("REST request to save Content : {}", content);
        if (content.getId() != null) {
            throw new BadRequestAlertException("A new content cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Content result = contentRepository.save(content);
        return ResponseEntity
            .created(new URI("/api/contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contents/:id} : Updates an existing content.
     *
     * @param id the id of the content to save.
     * @param content the content to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated content,
     * or with status {@code 400 (Bad Request)} if the content is not valid,
     * or with status {@code 500 (Internal Server Error)} if the content couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secure/contents/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable(value = "id", required = false) final Long id, @RequestBody Content content)
        throws URISyntaxException {
        log.debug("REST request to update Content : {}, {}", id, content);
        if (content.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, content.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Content result = contentRepository.save(content);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, content.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contents/:id} : Partial updates given fields of an existing content, field will ignore if it is null
     *
     * @param id the id of the content to save.
     * @param content the content to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated content,
     * or with status {@code 400 (Bad Request)} if the content is not valid,
     * or with status {@code 404 (Not Found)} if the content is not found,
     * or with status {@code 500 (Internal Server Error)} if the content couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/secure/contents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Content> partialUpdateContent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Content content
    ) throws URISyntaxException {
        log.debug("REST request to partial update Content partially : {}, {}", id, content);
        if (content.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, content.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Content> result = contentRepository
            .findById(content.getId())
            .map(
                existingContent -> {
                    if (content.getType() != null) {
                        existingContent.setType(content.getType());
                    }
                    if (content.getText() != null) {
                        existingContent.setText(content.getText());
                    }
                    if (content.getFilePath() != null) {
                        existingContent.setFilePath(content.getFilePath());
                    }
                    if (content.getSeqNum() != null) {
                        existingContent.setSeqNum(content.getSeqNum());
                    }

                    return existingContent;
                }
            )
            .map(contentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, content.getId().toString())
        );
    }

    /**
     * {@code GET  /contents} : get all the contents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contents in body.
     */
    @GetMapping("/contents")
    public List<Content> getAllContents() {
        log.debug("REST request to get all Contents");
        return contentRepository.findAll();
    }

    /**
     * {@code GET  /contents/:id} : get the "id" content.
     *
     * @param id the id of the content to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the content, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contents/{id}")
    public ResponseEntity<Content> getContent(@PathVariable Long id) {
        log.debug("REST request to get Content : {}", id);
        Optional<Content> content = contentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(content);
    }

    /**
     * {@code DELETE  /contents/:id} : delete the "id" content.
     *
     * @param id the id of the content to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secure/contents/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        log.debug("REST request to delete Content : {}", id);
        contentRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
