package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.repository.ExamStatusRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.com.tclinica.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExamStatusResource REST controller.
 *
 * @see ExamStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class ExamStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ExamStatusRepository examStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExamStatusMockMvc;

    private ExamStatus examStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamStatusResource examStatusResource = new ExamStatusResource(examStatusRepository);
        this.restExamStatusMockMvc = MockMvcBuilders.standaloneSetup(examStatusResource)
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
    public static ExamStatus createEntity(EntityManager em) {
        ExamStatus examStatus = new ExamStatus()
            .name(DEFAULT_NAME)
            .creationDate(DEFAULT_CREATION_DATE);
        return examStatus;
    }

    @Before
    public void initTest() {
        examStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamStatus() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();

        // Create the ExamStatus
        restExamStatusMockMvc.perform(post("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isCreated());

        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExamStatus.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createExamStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();

        // Create the ExamStatus with an existing ID
        examStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamStatusMockMvc.perform(post("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isBadRequest());

        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = examStatusRepository.findAll().size();
        // set the field null
        examStatus.setName(null);

        // Create the ExamStatus, which fails.

        restExamStatusMockMvc.perform(post("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isBadRequest());

        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = examStatusRepository.findAll().size();
        // set the field null
        examStatus.setCreationDate(null);

        // Create the ExamStatus, which fails.

        restExamStatusMockMvc.perform(post("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isBadRequest());

        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExamStatuses() throws Exception {
        // Initialize the database
        examStatusRepository.saveAndFlush(examStatus);

        // Get all the examStatusList
        restExamStatusMockMvc.perform(get("/api/exam-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))));
    }

    @Test
    @Transactional
    public void getExamStatus() throws Exception {
        // Initialize the database
        examStatusRepository.saveAndFlush(examStatus);

        // Get the examStatus
        restExamStatusMockMvc.perform(get("/api/exam-statuses/{id}", examStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(examStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingExamStatus() throws Exception {
        // Get the examStatus
        restExamStatusMockMvc.perform(get("/api/exam-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExamStatus() throws Exception {
        // Initialize the database
        examStatusRepository.saveAndFlush(examStatus);
        int databaseSizeBeforeUpdate = examStatusRepository.findAll().size();

        // Update the examStatus
        ExamStatus updatedExamStatus = examStatusRepository.findOne(examStatus.getId());
        updatedExamStatus
            .name(UPDATED_NAME)
            .creationDate(UPDATED_CREATION_DATE);

        restExamStatusMockMvc.perform(put("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExamStatus)))
            .andExpect(status().isOk());

        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeUpdate);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExamStatus.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingExamStatus() throws Exception {
        int databaseSizeBeforeUpdate = examStatusRepository.findAll().size();

        // Create the ExamStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExamStatusMockMvc.perform(put("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isCreated());

        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExamStatus() throws Exception {
        // Initialize the database
        examStatusRepository.saveAndFlush(examStatus);
        int databaseSizeBeforeDelete = examStatusRepository.findAll().size();

        // Get the examStatus
        restExamStatusMockMvc.perform(delete("/api/exam-statuses/{id}", examStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamStatus.class);
        ExamStatus examStatus1 = new ExamStatus();
        examStatus1.setId(1L);
        ExamStatus examStatus2 = new ExamStatus();
        examStatus2.setId(examStatus1.getId());
        assertThat(examStatus1).isEqualTo(examStatus2);
        examStatus2.setId(2L);
        assertThat(examStatus1).isNotEqualTo(examStatus2);
        examStatus1.setId(null);
        assertThat(examStatus1).isNotEqualTo(examStatus2);
    }
}
