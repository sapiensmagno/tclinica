package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.MedicalRecord;
import br.com.tclinica.repository.MedicalRecordRepository;
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
 * Test class for the MedicalRecordResource REST controller.
 *
 * @see MedicalRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class MedicalRecordResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicalRecordMockMvc;

    private MedicalRecord medicalRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicalRecordResource medicalRecordResource = new MedicalRecordResource(medicalRecordRepository);
        this.restMedicalRecordMockMvc = MockMvcBuilders.standaloneSetup(medicalRecordResource)
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
    public static MedicalRecord createEntity(EntityManager em) {
        MedicalRecord medicalRecord = new MedicalRecord()
            .description(DEFAULT_DESCRIPTION);
        return medicalRecord;
    }

    @Before
    public void initTest() {
        medicalRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicalRecord() throws Exception {
        int databaseSizeBeforeCreate = medicalRecordRepository.findAll().size();

        // Create the MedicalRecord
        restMedicalRecordMockMvc.perform(post("/api/medical-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalRecord)))
            .andExpect(status().isCreated());

        // Validate the MedicalRecord in the database
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAll();
        assertThat(medicalRecordList).hasSize(databaseSizeBeforeCreate + 1);
        MedicalRecord testMedicalRecord = medicalRecordList.get(medicalRecordList.size() - 1);
        assertThat(testMedicalRecord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMedicalRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalRecordRepository.findAll().size();

        // Create the MedicalRecord with an existing ID
        medicalRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalRecordMockMvc.perform(post("/api/medical-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalRecord)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAll();
        assertThat(medicalRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMedicalRecords() throws Exception {
        // Initialize the database
        medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get all the medicalRecordList
        restMedicalRecordMockMvc.perform(get("/api/medical-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicalRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMedicalRecord() throws Exception {
        // Initialize the database
        medicalRecordRepository.saveAndFlush(medicalRecord);

        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(get("/api/medical-records/{id}", medicalRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicalRecord.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicalRecord() throws Exception {
        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(get("/api/medical-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicalRecord() throws Exception {
        // Initialize the database
        medicalRecordRepository.saveAndFlush(medicalRecord);
        int databaseSizeBeforeUpdate = medicalRecordRepository.findAll().size();

        // Update the medicalRecord
        MedicalRecord updatedMedicalRecord = medicalRecordRepository.findOne(medicalRecord.getId());
        updatedMedicalRecord
            .description(UPDATED_DESCRIPTION);

        restMedicalRecordMockMvc.perform(put("/api/medical-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicalRecord)))
            .andExpect(status().isOk());

        // Validate the MedicalRecord in the database
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAll();
        assertThat(medicalRecordList).hasSize(databaseSizeBeforeUpdate);
        MedicalRecord testMedicalRecord = medicalRecordList.get(medicalRecordList.size() - 1);
        assertThat(testMedicalRecord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicalRecord() throws Exception {
        int databaseSizeBeforeUpdate = medicalRecordRepository.findAll().size();

        // Create the MedicalRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicalRecordMockMvc.perform(put("/api/medical-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicalRecord)))
            .andExpect(status().isCreated());

        // Validate the MedicalRecord in the database
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAll();
        assertThat(medicalRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicalRecord() throws Exception {
        // Initialize the database
        medicalRecordRepository.saveAndFlush(medicalRecord);
        int databaseSizeBeforeDelete = medicalRecordRepository.findAll().size();

        // Get the medicalRecord
        restMedicalRecordMockMvc.perform(delete("/api/medical-records/{id}", medicalRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAll();
        assertThat(medicalRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicalRecord.class);
        MedicalRecord medicalRecord1 = new MedicalRecord();
        medicalRecord1.setId(1L);
        MedicalRecord medicalRecord2 = new MedicalRecord();
        medicalRecord2.setId(medicalRecord1.getId());
        assertThat(medicalRecord1).isEqualTo(medicalRecord2);
        medicalRecord2.setId(2L);
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);
        medicalRecord1.setId(null);
        assertThat(medicalRecord1).isNotEqualTo(medicalRecord2);
    }
}
