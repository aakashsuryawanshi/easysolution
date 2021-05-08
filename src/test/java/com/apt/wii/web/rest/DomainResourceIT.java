package com.apt.wii.web.rest;

import com.apt.wii.WiiApp;
import com.apt.wii.config.TestSecurityConfiguration;
import com.apt.wii.domain.Domain;
import com.apt.wii.repository.DomainRepository;

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
 * Integration tests for the {@link DomainResource} REST controller.
 */
@SpringBootTest(classes = { WiiApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class DomainResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomainMockMvc;

    private Domain domain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createEntity(EntityManager em) {
        Domain domain = new Domain()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return domain;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createUpdatedEntity(EntityManager em) {
        Domain domain = new Domain()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return domain;
    }

    @BeforeEach
    public void initTest() {
        domain = createEntity(em);
    }

    @Test
    @Transactional
    public void createDomain() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();
        // Create the Domain
        restDomainMockMvc.perform(post("/api/domains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isCreated());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate + 1);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomain.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDomainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // Create the Domain with an existing ID
        domain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomainMockMvc.perform(post("/api/domains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDomains() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList
        restDomainMockMvc.perform(get("/api/domains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domain.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingDomain() throws Exception {
        // Get the domain
        restDomainMockMvc.perform(get("/api/domains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain
        Domain updatedDomain = domainRepository.findById(domain.getId()).get();
        // Disconnect from session so that the updates on updatedDomain are not directly saved in db
        em.detach(updatedDomain);
        updatedDomain
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restDomainMockMvc.perform(put("/api/domains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDomain)))
            .andExpect(status().isOk());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDomain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomainMockMvc.perform(put("/api/domains").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(domain)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeDelete = domainRepository.findAll().size();

        // Delete the domain
        restDomainMockMvc.perform(delete("/api/domains/{id}", domain.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
