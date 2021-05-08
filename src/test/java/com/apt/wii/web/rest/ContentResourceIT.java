package com.apt.wii.web.rest;

import com.apt.wii.WiiApp;
import com.apt.wii.config.TestSecurityConfiguration;
import com.apt.wii.domain.Content;
import com.apt.wii.repository.ContentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apt.wii.domain.enumeration.ContentType;
/**
 * Integration tests for the {@link ContentResource} REST controller.
 */
@SpringBootTest(classes = { WiiApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ContentResourceIT {

    private static final ContentType DEFAULT_TYPE = ContentType.PARA;
    private static final ContentType UPDATED_TYPE = ContentType.IMAGE;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQ_NUM = 1;
    private static final Integer UPDATED_SEQ_NUM = 2;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentMockMvc;

    private Content content;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createEntity(EntityManager em) {
        Content content = new Content()
            .type(DEFAULT_TYPE)
            .text(DEFAULT_TEXT)
            .filePath(DEFAULT_FILE_PATH)
            .seqNum(DEFAULT_SEQ_NUM);
        return content;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Content createUpdatedEntity(EntityManager em) {
        Content content = new Content()
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .filePath(UPDATED_FILE_PATH)
            .seqNum(UPDATED_SEQ_NUM);
        return content;
    }

    @BeforeEach
    public void initTest() {
        content = createEntity(em);
    }

    @Test
    @Transactional
    public void createContent() throws Exception {
        int databaseSizeBeforeCreate = contentRepository.findAll().size();
        // Create the Content
        restContentMockMvc.perform(post("/api/contents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(content)))
            .andExpect(status().isCreated());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate + 1);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContent.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testContent.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testContent.getSeqNum()).isEqualTo(DEFAULT_SEQ_NUM);
    }

    @Test
    @Transactional
    public void createContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentRepository.findAll().size();

        // Create the Content with an existing ID
        content.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentMockMvc.perform(post("/api/contents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(content)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllContents() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        // Get all the contentList
        restContentMockMvc.perform(get("/api/contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(content.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].seqNum").value(hasItem(DEFAULT_SEQ_NUM)));
    }
    
    @Test
    @Transactional
    public void getContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        // Get the content
        restContentMockMvc.perform(get("/api/contents/{id}", content.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(content.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.seqNum").value(DEFAULT_SEQ_NUM));
    }
    @Test
    @Transactional
    public void getNonExistingContent() throws Exception {
        // Get the content
        restContentMockMvc.perform(get("/api/contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeUpdate = contentRepository.findAll().size();

        // Update the content
        Content updatedContent = contentRepository.findById(content.getId()).get();
        // Disconnect from session so that the updates on updatedContent are not directly saved in db
        em.detach(updatedContent);
        updatedContent
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .filePath(UPDATED_FILE_PATH)
            .seqNum(UPDATED_SEQ_NUM);

        restContentMockMvc.perform(put("/api/contents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContent)))
            .andExpect(status().isOk());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
        Content testContent = contentList.get(contentList.size() - 1);
        assertThat(testContent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContent.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testContent.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testContent.getSeqNum()).isEqualTo(UPDATED_SEQ_NUM);
    }

    @Test
    @Transactional
    public void updateNonExistingContent() throws Exception {
        int databaseSizeBeforeUpdate = contentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentMockMvc.perform(put("/api/contents").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(content)))
            .andExpect(status().isBadRequest());

        // Validate the Content in the database
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContent() throws Exception {
        // Initialize the database
        contentRepository.saveAndFlush(content);

        int databaseSizeBeforeDelete = contentRepository.findAll().size();

        // Delete the content
        restContentMockMvc.perform(delete("/api/contents/{id}", content.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Content> contentList = contentRepository.findAll();
        assertThat(contentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
