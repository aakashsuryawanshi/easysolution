package com.apt.wii.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apt.wii.IntegrationTest;
import com.apt.wii.domain.Semester;
import com.apt.wii.repository.SemesterRepository;
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
 * Integration tests for the {@link SemesterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SemesterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/semesters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemesterMockMvc;

    private Semester semester;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semester createEntity(EntityManager em) {
        Semester semester = new Semester().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return semester;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semester createUpdatedEntity(EntityManager em) {
        Semester semester = new Semester().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return semester;
    }

    @BeforeEach
    public void initTest() {
        semester = createEntity(em);
    }

    @Test
    @Transactional
    void createSemester() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();
        // Create the Semester
        restSemesterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isCreated());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate + 1);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSemester.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSemesterWithExistingId() throws Exception {
        // Create the Semester with an existing ID
        semester.setId(1L);

        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemesterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSemesters() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList
        restSemesterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semester.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get the semester
        restSemesterMockMvc
            .perform(get(ENTITY_API_URL_ID, semester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semester.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSemester() throws Exception {
        // Get the semester
        restSemesterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester
        Semester updatedSemester = semesterRepository.findById(semester.getId()).get();
        // Disconnect from session so that the updates on updatedSemester are not directly saved in db
        em.detach(updatedSemester);
        updatedSemester.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSemesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSemester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSemester))
            )
            .andExpect(status().isOk());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSemester.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemesterWithPatch() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester using partial update
        Semester partialUpdatedSemester = new Semester();
        partialUpdatedSemester.setId(semester.getId());

        partialUpdatedSemester.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSemesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemester))
            )
            .andExpect(status().isOk());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSemester.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSemesterWithPatch() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester using partial update
        Semester partialUpdatedSemester = new Semester();
        partialUpdatedSemester.setId(semester.getId());

        partialUpdatedSemester.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSemesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemester))
            )
            .andExpect(status().isOk());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSemester.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semester.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semester))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();
        semester.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemesterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeDelete = semesterRepository.findAll().size();

        // Delete the semester
        restSemesterMockMvc
            .perform(delete(ENTITY_API_URL_ID, semester.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
