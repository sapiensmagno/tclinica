package br.com.tclinica.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.DoctorRepository;
import br.com.tclinica.service.DoctorService;
import br.com.tclinica.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the DoctorResource REST controller.
 *
 * @see DoctorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class DoctorResourceIntTest {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorResource doctorResource = new DoctorResource(doctorService);
        this.restDoctorMockMvc = MockMvcBuilders.standaloneSetup(doctorResource)
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
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .nickname(DEFAULT_NICKNAME)
            .inactive(DEFAULT_INACTIVE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        doctor.setUser(user);
        return doctor;
    }

    @Before
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testDoctor.isInactive()).isEqualTo(DEFAULT_INACTIVE);
    }

    @Test
    @Transactional
    public void createDoctorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor with an existing ID
        doctor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorService.save(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findOne(doctor.getId());
        updatedDoctor
            .nickname(UPDATED_NICKNAME)
            .inactive(UPDATED_INACTIVE);

        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctor)))
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testDoctor.isInactive()).isEqualTo(UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Create the Doctor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctor)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorService.save(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Get the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctor.isInactive());
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctor.class);
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        Doctor doctor2 = new Doctor();
        doctor2.setId(doctor1.getId());
        assertThat(doctor1).isEqualTo(doctor2);
        doctor2.setId(2L);
        assertThat(doctor1).isNotEqualTo(doctor2);
        doctor1.setId(null);
        assertThat(doctor1).isNotEqualTo(doctor2);
    }
}
