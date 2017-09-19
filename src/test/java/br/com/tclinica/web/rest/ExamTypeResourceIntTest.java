package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.ExamType;
import br.com.tclinica.repository.ExamTypeRepository;
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
 * Test class for the ExamTypeResource REST controller.
 *
 * @see ExamTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class ExamTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExamTypeMockMvc;

    private ExamType examType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamTypeResource examTypeResource = new ExamTypeResource(examTypeRepository);
        this.restExamTypeMockMvc = MockMvcBuilders.standaloneSetup(examTypeResource)
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
    public static ExamType createEntity(EntityManager em) {
        ExamType examType = new ExamType()
            .name(DEFAULT_NAME);
        return examType;
    }

    @Before
    public void initTest() {
        examType = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllExamTypes() throws Exception {
        // Initialize the database
        examTypeRepository.saveAndFlush(examType);

        // Get all the examTypeList
        restExamTypeMockMvc.perform(get("/api/exam-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getExamType() throws Exception {
        // Initialize the database
        examTypeRepository.saveAndFlush(examType);

        // Get the examType
        restExamTypeMockMvc.perform(get("/api/exam-types/{id}", examType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(examType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExamType() throws Exception {
        // Get the examType
        restExamTypeMockMvc.perform(get("/api/exam-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteExamType() throws Exception {
        // Initialize the database
        examTypeRepository.saveAndFlush(examType);
        int databaseSizeBeforeDelete = examTypeRepository.findAll().size();

        // Get the examType
        restExamTypeMockMvc.perform(delete("/api/exam-types/{id}", examType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExamType> examTypeList = examTypeRepository.findAll();
        assertThat(examTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamType.class);
        ExamType examType1 = new ExamType();
        examType1.setId(1L);
        ExamType examType2 = new ExamType();
        examType2.setId(examType1.getId());
        assertThat(examType1).isEqualTo(examType2);
        examType2.setId(2L);
        assertThat(examType1).isNotEqualTo(examType2);
        examType1.setId(null);
        assertThat(examType1).isNotEqualTo(examType2);
    }
}
