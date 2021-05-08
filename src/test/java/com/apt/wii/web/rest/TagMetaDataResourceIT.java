package com.apt.wii.web.rest;

import com.apt.wii.WiiApp;
import com.apt.wii.config.TestSecurityConfiguration;
import com.apt.wii.domain.TagMetaData;
import com.apt.wii.repository.TagMetaDataRepository;

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

/**
 * Integration tests for the {@link TagMetaDataResource} REST controller.
 */
@SpringBootTest(classes = { WiiApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TagMetaDataResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private TagMetaDataRepository tagMetaDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMetaDataMockMvc;

    private TagMetaData tagMetaData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagMetaData createEntity(EntityManager em) {
        TagMetaData tagMetaData = new TagMetaData()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return tagMetaData;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagMetaData createUpdatedEntity(EntityManager em) {
        TagMetaData tagMetaData = new TagMetaData()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        return tagMetaData;
    }

    @BeforeEach
    public void initTest() {
        tagMetaData = createEntity(em);
    }

    @Test
    @Transactional
    public void createTagMetaData() throws Exception {
        int databaseSizeBeforeCreate = tagMetaDataRepository.findAll().size();
        // Create the TagMetaData
        restTagMetaDataMockMvc.perform(post("/api/tag-meta-data").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
            .andExpect(status().isCreated());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeCreate + 1);
        TagMetaData testTagMetaData = tagMetaDataList.get(tagMetaDataList.size() - 1);
        assertThat(testTagMetaData.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTagMetaData.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createTagMetaDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagMetaDataRepository.findAll().size();

        // Create the TagMetaData with an existing ID
        tagMetaData.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMetaDataMockMvc.perform(post("/api/tag-meta-data").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        // Get all the tagMetaDataList
        restTagMetaDataMockMvc.perform(get("/api/tag-meta-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagMetaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        // Get the tagMetaData
        restTagMetaDataMockMvc.perform(get("/api/tag-meta-data/{id}", tagMetaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagMetaData.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }
    @Test
    @Transactional
    public void getNonExistingTagMetaData() throws Exception {
        // Get the tagMetaData
        restTagMetaDataMockMvc.perform(get("/api/tag-meta-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();

        // Update the tagMetaData
        TagMetaData updatedTagMetaData = tagMetaDataRepository.findById(tagMetaData.getId()).get();
        // Disconnect from session so that the updates on updatedTagMetaData are not directly saved in db
        em.detach(updatedTagMetaData);
        updatedTagMetaData
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);

        restTagMetaDataMockMvc.perform(put("/api/tag-meta-data").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTagMetaData)))
            .andExpect(status().isOk());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
        TagMetaData testTagMetaData = tagMetaDataList.get(tagMetaDataList.size() - 1);
        assertThat(testTagMetaData.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTagMetaData.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc.perform(put("/api/tag-meta-data").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeDelete = tagMetaDataRepository.findAll().size();

        // Delete the tagMetaData
        restTagMetaDataMockMvc.perform(delete("/api/tag-meta-data/{id}", tagMetaData.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
