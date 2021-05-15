package com.apt.wii.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apt.wii.IntegrationTest;
import com.apt.wii.domain.Domain;
import com.apt.wii.repository.DomainRepository;
import com.apt.wii.service.dto.DomainDTO;
import com.apt.wii.service.mapper.DomainMapper;
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
 * Integration tests for the {@link DomainResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomainResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/domains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private DomainMapper domainMapper;

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
        Domain domain = new Domain().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return domain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domain createUpdatedEntity(EntityManager em) {
        Domain domain = new Domain().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return domain;
    }

    @BeforeEach
    public void initTest() {
        domain = createEntity(em);
    }

    @Test
    @Transactional
    void createDomain() throws Exception {
        int databaseSizeBeforeCreate = domainRepository.findAll().size();
        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);
        restDomainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domainDTO)))
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
    void createDomainWithExistingId() throws Exception {
        // Create the Domain with an existing ID
        domain.setId(1L);
        DomainDTO domainDTO = domainMapper.toDto(domain);

        int databaseSizeBeforeCreate = domainRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomainMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDomains() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get all the domainList
        restDomainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domain.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        // Get the domain
        restDomainMockMvc
            .perform(get(ENTITY_API_URL_ID, domain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domain.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDomain() throws Exception {
        // Get the domain
        restDomainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain
        Domain updatedDomain = domainRepository.findById(domain.getId()).get();
        // Disconnect from session so that the updates on updatedDomain are not directly saved in db
        em.detach(updatedDomain);
        updatedDomain.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        DomainDTO domainDTO = domainMapper.toDto(updatedDomain);

        restDomainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domainDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
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
    void putNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domainDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domainDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomainWithPatch() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain using partial update
        Domain partialUpdatedDomain = new Domain();
        partialUpdatedDomain.setId(domain.getId());

        partialUpdatedDomain.description(UPDATED_DESCRIPTION);

        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomain))
            )
            .andExpect(status().isOk());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
        Domain testDomain = domainList.get(domainList.size() - 1);
        assertThat(testDomain.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDomain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDomainWithPatch() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeUpdate = domainRepository.findAll().size();

        // Update the domain using partial update
        Domain partialUpdatedDomain = new Domain();
        partialUpdatedDomain.setId(domain.getId());

        partialUpdatedDomain.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomain.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomain))
            )
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
    void patchNonExistingDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domainDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomain() throws Exception {
        int databaseSizeBeforeUpdate = domainRepository.findAll().size();
        domain.setId(count.incrementAndGet());

        // Create the Domain
        DomainDTO domainDTO = domainMapper.toDto(domain);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomainMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(domainDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domain in the database
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomain() throws Exception {
        // Initialize the database
        domainRepository.saveAndFlush(domain);

        int databaseSizeBeforeDelete = domainRepository.findAll().size();

        // Delete the domain
        restDomainMockMvc
            .perform(delete(ENTITY_API_URL_ID, domain.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domain> domainList = domainRepository.findAll();
        assertThat(domainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
