package com.apt.wii.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apt.wii.IntegrationTest;
import com.apt.wii.domain.TagMetaData;
import com.apt.wii.repository.TagMetaDataRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TagMetaDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagMetaDataResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tag-meta-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        TagMetaData tagMetaData = new TagMetaData().key(DEFAULT_KEY).value(DEFAULT_VALUE);
        return tagMetaData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagMetaData createUpdatedEntity(EntityManager em) {
        TagMetaData tagMetaData = new TagMetaData().key(UPDATED_KEY).value(UPDATED_VALUE);
        return tagMetaData;
    }

    @BeforeEach
    public void initTest() {
        tagMetaData = createEntity(em);
    }

    @Test
    @Transactional
    void createTagMetaData() throws Exception {
        int databaseSizeBeforeCreate = tagMetaDataRepository.findAll().size();
        // Create the TagMetaData
        restTagMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
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
    void createTagMetaDataWithExistingId() throws Exception {
        // Create the TagMetaData with an existing ID
        tagMetaData.setId(1L);

        int databaseSizeBeforeCreate = tagMetaDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMetaDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        // Get all the tagMetaDataList
        restTagMetaDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagMetaData.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        // Get the tagMetaData
        restTagMetaDataMockMvc
            .perform(get(ENTITY_API_URL_ID, tagMetaData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagMetaData.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingTagMetaData() throws Exception {
        // Get the tagMetaData
        restTagMetaDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();

        // Update the tagMetaData
        TagMetaData updatedTagMetaData = tagMetaDataRepository.findById(tagMetaData.getId()).get();
        // Disconnect from session so that the updates on updatedTagMetaData are not directly saved in db
        em.detach(updatedTagMetaData);
        updatedTagMetaData.key(UPDATED_KEY).value(UPDATED_VALUE);

        restTagMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTagMetaData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTagMetaData))
            )
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
    void putNonExistingTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagMetaData.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagMetaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagMetaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagMetaData)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagMetaDataWithPatch() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();

        // Update the tagMetaData using partial update
        TagMetaData partialUpdatedTagMetaData = new TagMetaData();
        partialUpdatedTagMetaData.setId(tagMetaData.getId());

        partialUpdatedTagMetaData.key(UPDATED_KEY).value(UPDATED_VALUE);

        restTagMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagMetaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagMetaData))
            )
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
    void fullUpdateTagMetaDataWithPatch() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();

        // Update the tagMetaData using partial update
        TagMetaData partialUpdatedTagMetaData = new TagMetaData();
        partialUpdatedTagMetaData.setId(tagMetaData.getId());

        partialUpdatedTagMetaData.key(UPDATED_KEY).value(UPDATED_VALUE);

        restTagMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagMetaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagMetaData))
            )
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
    void patchNonExistingTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagMetaData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagMetaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagMetaData))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTagMetaData() throws Exception {
        int databaseSizeBeforeUpdate = tagMetaDataRepository.findAll().size();
        tagMetaData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMetaDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tagMetaData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagMetaData in the database
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTagMetaData() throws Exception {
        // Initialize the database
        tagMetaDataRepository.saveAndFlush(tagMetaData);

        int databaseSizeBeforeDelete = tagMetaDataRepository.findAll().size();

        // Delete the tagMetaData
        restTagMetaDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagMetaData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TagMetaData> tagMetaDataList = tagMetaDataRepository.findAll();
        assertThat(tagMetaDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
