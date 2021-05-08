package com.apt.wii.web.rest;

import com.apt.wii.WiiApp;
import com.apt.wii.config.TestSecurityConfiguration;
import com.apt.wii.domain.Semester;
import com.apt.wii.repository.SemesterRepository;

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
 * Integration tests for the {@link SemesterResource} REST controller.
 */
@SpringBootTest(classes = { WiiApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SemesterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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
        Semester semester = new Semester()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return semester;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semester createUpdatedEntity(EntityManager em) {
        Semester semester = new Semester()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return semester;
    }

    @BeforeEach
    public void initTest() {
        semester = createEntity(em);
    }

    @Test
    @Transactional
    public void createSemester() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();
        // Create the Semester
        restSemesterMockMvc.perform(post("/api/semesters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semester)))
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
    public void createSemesterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // Create the Semester with an existing ID
        semester.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemesterMockMvc.perform(post("/api/semesters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSemesters() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList
        restSemesterMockMvc.perform(get("/api/semesters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semester.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", semester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semester.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingSemester() throws Exception {
        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester
        Semester updatedSemester = semesterRepository.findById(semester.getId()).get();
        // Disconnect from session so that the updates on updatedSemester are not directly saved in db
        em.detach(updatedSemester);
        updatedSemester
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restSemesterMockMvc.perform(put("/api/semesters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSemester)))
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
    public void updateNonExistingSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterMockMvc.perform(put("/api/semesters").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semester)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeDelete = semesterRepository.findAll().size();

        // Delete the semester
        restSemesterMockMvc.perform(delete("/api/semesters/{id}", semester.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
