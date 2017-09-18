package br.com.tclinica.web.rest;

import static br.com.tclinica.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;

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

import br.com.tclinica.TclinicaApp;
import br.com.tclinica.domain.Exam;
import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.domain.enumeration.ExamStatuses;
import br.com.tclinica.repository.ExamRepository;
import br.com.tclinica.repository.ExamStatusRepository;
import br.com.tclinica.service.ExamStatusService;
import br.com.tclinica.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the ExamStatusResource REST controller.
 *
 * @see ExamStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class ExamStatusResourceIntTest {

    private static final ExamStatuses FIRST_NAME = ExamStatuses.LAB_REQUEST;
    private static final ExamStatuses SECOND_NAME = ExamStatuses.TO_DOC;
    private static final ExamStatuses THIRD_NAME = ExamStatuses.TO_PATIENT;
    private static final ExamStatuses FORTH_NAME = ExamStatuses.ARCHIVE;

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ExamStatusRepository examStatusRepository;
    
    @Autowired
    private ExamStatusService examStatusService;
    
    @Autowired
    private ExamRepository examRepository;
    
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
        final ExamStatusResource examStatusResource = new ExamStatusResource(examStatusRepository,
        		examStatusService, examRepository);
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
            .name(FIRST_NAME.toString())
            .creationDate(DEFAULT_CREATION_DATE);
        return examStatus;
    }

    @Before
    public void initTest() {
        examStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createExamStatus_Lab() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();
        
        sendPostToEndpoint("labRequest");
        
        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(FIRST_NAME.toString());
    }
    
    @Test
    @Transactional
    public void createExamStatus_Doctor() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();
        
        sendPostToEndpoint("toDoctor");
        
        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(SECOND_NAME.toString());
    }
    
    @Test
    @Transactional
    public void createExamStatus_Patient() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();
        
        sendPostToEndpoint("toPatient");
        
        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(THIRD_NAME.toString());
    }
    
    @Test
    @Transactional
    public void createExamStatus_Archive() throws Exception {
        int databaseSizeBeforeCreate = examStatusRepository.findAll().size();
        
        sendPostToEndpoint("archive");
        
        // Validate the ExamStatus in the database
        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ExamStatus testExamStatus = examStatusList.get(examStatusList.size() - 1);
        assertThat(testExamStatus.getName()).isEqualTo(FORTH_NAME.toString());
    }
    
    private void sendPostToEndpoint (String endPoint) throws Exception {
        // Create the ExamStatus
    	Exam exam = ExamResourceIntTest.createEntity(this.em);
        examRepository.save(exam);
        
        restExamStatusMockMvc.perform(post("/api/exam-statuses/" + endPoint)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isCreated());
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
            .andExpect(jsonPath("$.[*].name").value(hasItem(FIRST_NAME.toString())))
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
            .andExpect(jsonPath("$.name").value(FIRST_NAME.toString()))
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

        ExamStatus updatedExamStatus = examStatusRepository.findOne(examStatus.getId());
        updatedExamStatus
            .name(SECOND_NAME.toString())
            .creationDate(UPDATED_CREATION_DATE);

        restExamStatusMockMvc.perform(put("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExamStatus)))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Transactional
    public void updateNonExistingExamStatus() throws Exception {
        int databaseSizeBeforeUpdate = examStatusRepository.findAll().size();

        restExamStatusMockMvc.perform(put("/api/exam-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(examStatus)))
            .andExpect(status().isMethodNotAllowed());

        List<ExamStatus> examStatusList = examStatusRepository.findAll();
        assertThat(examStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExamStatus() throws Exception {
        // Initialize the database
        examStatusRepository.saveAndFlush(examStatus);
        int databaseSizeBeforeDelete = examStatusRepository.findAll().size();

        restExamStatusMockMvc.perform(delete("/api/exam-statuses/{id}", examStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isMethodNotAllowed());

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
