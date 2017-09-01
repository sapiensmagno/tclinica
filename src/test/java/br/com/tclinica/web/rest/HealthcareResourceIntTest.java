package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.Healthcare;
import br.com.tclinica.repository.HealthcareRepository;
import br.com.tclinica.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HealthcareResource REST controller.
 *
 * @see HealthcareResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class HealthcareResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HealthcareRepository healthcareRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHealthcareMockMvc;

    private Healthcare healthcare;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HealthcareResource healthcareResource = new HealthcareResource(healthcareRepository);
        this.restHealthcareMockMvc = MockMvcBuilders.standaloneSetup(healthcareResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Healthcare createEntity(EntityManager em) {
        Healthcare healthcare = new Healthcare()
            .name(DEFAULT_NAME);
        return healthcare;
    }

    @Before
    public void initTest() {
        healthcare = createEntity(em);
    }

    @Test
    @Transactional
    public void createHealthcare() throws Exception {
        int databaseSizeBeforeCreate = healthcareRepository.findAll().size();

        // Create the Healthcare
        restHealthcareMockMvc.perform(post("/api/healthcares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcare)))
            .andExpect(status().isCreated());

        // Validate the Healthcare in the database
        List<Healthcare> healthcareList = healthcareRepository.findAll();
        assertThat(healthcareList).hasSize(databaseSizeBeforeCreate + 1);
        Healthcare testHealthcare = healthcareList.get(healthcareList.size() - 1);
        assertThat(testHealthcare.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createHealthcareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = healthcareRepository.findAll().size();

        // Create the Healthcare with an existing ID
        healthcare.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthcareMockMvc.perform(post("/api/healthcares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcare)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Healthcare> healthcareList = healthcareRepository.findAll();
        assertThat(healthcareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHealthcares() throws Exception {
        // Initialize the database
        healthcareRepository.saveAndFlush(healthcare);

        // Get all the healthcareList
        restHealthcareMockMvc.perform(get("/api/healthcares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthcare.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHealthcare() throws Exception {
        // Initialize the database
        healthcareRepository.saveAndFlush(healthcare);

        // Get the healthcare
        restHealthcareMockMvc.perform(get("/api/healthcares/{id}", healthcare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(healthcare.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHealthcare() throws Exception {
        // Get the healthcare
        restHealthcareMockMvc.perform(get("/api/healthcares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHealthcare() throws Exception {
        // Initialize the database
        healthcareRepository.saveAndFlush(healthcare);
        int databaseSizeBeforeUpdate = healthcareRepository.findAll().size();

        // Update the healthcare
        Healthcare updatedHealthcare = healthcareRepository.findOne(healthcare.getId());
        updatedHealthcare
            .name(UPDATED_NAME);

        restHealthcareMockMvc.perform(put("/api/healthcares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHealthcare)))
            .andExpect(status().isOk());

        // Validate the Healthcare in the database
        List<Healthcare> healthcareList = healthcareRepository.findAll();
        assertThat(healthcareList).hasSize(databaseSizeBeforeUpdate);
        Healthcare testHealthcare = healthcareList.get(healthcareList.size() - 1);
        assertThat(testHealthcare.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingHealthcare() throws Exception {
        int databaseSizeBeforeUpdate = healthcareRepository.findAll().size();

        // Create the Healthcare

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHealthcareMockMvc.perform(put("/api/healthcares")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcare)))
            .andExpect(status().isCreated());

        // Validate the Healthcare in the database
        List<Healthcare> healthcareList = healthcareRepository.findAll();
        assertThat(healthcareList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHealthcare() throws Exception {
        // Initialize the database
        healthcareRepository.saveAndFlush(healthcare);
        int databaseSizeBeforeDelete = healthcareRepository.findAll().size();

        // Get the healthcare
        restHealthcareMockMvc.perform(delete("/api/healthcares/{id}", healthcare.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Healthcare> healthcareList = healthcareRepository.findAll();
        assertThat(healthcareList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Healthcare.class);
        Healthcare healthcare1 = new Healthcare();
        healthcare1.setId(1L);
        Healthcare healthcare2 = new Healthcare();
        healthcare2.setId(healthcare1.getId());
        assertThat(healthcare1).isEqualTo(healthcare2);
        healthcare2.setId(2L);
        assertThat(healthcare1).isNotEqualTo(healthcare2);
        healthcare1.setId(null);
        assertThat(healthcare1).isNotEqualTo(healthcare2);
    }
}
