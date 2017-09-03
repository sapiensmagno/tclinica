package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.repository.DoctorScheduleRepository;
import br.com.tclinica.service.DoctorScheduleService;
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
import javax.validation.constraints.AssertFalse;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DoctorScheduleResource REST controller.
 *
 * @see DoctorScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class DoctorScheduleResourceIntTest {

    private static final Integer DEFAULT_APPOINTMENTS_DURATION_MINUTES = 1;
    private static final Integer UPDATED_APPOINTMENTS_DURATION_MINUTES = 2;

    private static final Integer DEFAULT_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES = 1;
    private static final Integer UPDATED_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES = 2;

    private static final Instant DEFAULT_EARLIEST_APPOINTMENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EARLIEST_APPOINTMENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LATEST_APPOINTMENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LATEST_APPOINTMENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CALENDAR_ID = "AAAAAAAAAA";
    private static final String UPDATED_CALENDAR_ID = "BBBBBBBBBB";

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private DoctorScheduleService doctorScheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDoctorScheduleMockMvc;

    private DoctorSchedule doctorSchedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorScheduleResource doctorScheduleResource = new DoctorScheduleResource(doctorScheduleService);
        this.restDoctorScheduleMockMvc = MockMvcBuilders.standaloneSetup(doctorScheduleResource)
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
    public static DoctorSchedule createEntity(EntityManager em) {
        DoctorSchedule doctorSchedule = new DoctorSchedule()
            .appointmentsDurationMinutes(DEFAULT_APPOINTMENTS_DURATION_MINUTES)
            .intervalBetweenAppointmentsMinutes(DEFAULT_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES)
            .earliestAppointmentTime(DEFAULT_EARLIEST_APPOINTMENT_TIME)
            .latestAppointmentTime(DEFAULT_LATEST_APPOINTMENT_TIME)
            .calendarId(DEFAULT_CALENDAR_ID);
        return doctorSchedule;
    }

    @Before
    public void initTest() {
        doctorSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorSchedule() throws Exception {
    	int databaseSizeBeforeCreate = doctorScheduleRepository.findAll().size();
         
        // Create the DoctorSchedule
        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isCreated());

        // Validate the DoctorSchedule in the database
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorSchedule testDoctorSchedule = doctorScheduleList.get(doctorScheduleList.size() - 1);
        assertThat(testDoctorSchedule.getAppointmentsDurationMinutes()).isEqualTo(DEFAULT_APPOINTMENTS_DURATION_MINUTES);
        assertThat(testDoctorSchedule.getIntervalBetweenAppointmentsMinutes()).isEqualTo(DEFAULT_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES);
        assertThat(testDoctorSchedule.getEarliestAppointmentTime()).isEqualTo(DEFAULT_EARLIEST_APPOINTMENT_TIME);
        assertThat(testDoctorSchedule.getLatestAppointmentTime()).isEqualTo(DEFAULT_LATEST_APPOINTMENT_TIME);
        assertThat(testDoctorSchedule.getCalendarId()).isEqualTo(DEFAULT_CALENDAR_ID);
    }
    
    @Test
    @Transactional
    public void DoctorScheduleMustHaveADoctor () throws Exception {
        int databaseSizeBeforeCreate = doctorScheduleRepository.findAll().size();

        // Create the DoctorSchedule with an existing ID
        doctorSchedule.setDoctor(null);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the entity in the database
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void createDoctorScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorScheduleRepository.findAll().size();

        // Create the DoctorSchedule with an existing ID
        doctorSchedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        // Validate in the database
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAppointmentsDurationMinutesIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorScheduleRepository.findAll().size();
        // set the field null
        doctorSchedule.setAppointmentsDurationMinutes(null);

        // Create the DoctorSchedule, which fails.

        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEarliestAppointmentTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorScheduleRepository.findAll().size();
        // set the field null
        doctorSchedule.setEarliestAppointmentTime(null);

        // Create the DoctorSchedule, which fails.

        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatestAppointmentTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = doctorScheduleRepository.findAll().size();
        // set the field null
        doctorSchedule.setLatestAppointmentTime(null);

        // Create the DoctorSchedule, which fails.

        restDoctorScheduleMockMvc.perform(post("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoctorSchedules() throws Exception {
        // Initialize the database
        doctorScheduleRepository.saveAndFlush(doctorSchedule);

        // Get all the doctorScheduleList
        restDoctorScheduleMockMvc.perform(get("/api/doctor-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].appointmentsDurationMinutes").value(hasItem(DEFAULT_APPOINTMENTS_DURATION_MINUTES)))
            .andExpect(jsonPath("$.[*].intervalBetweenAppointmentsMinutes").value(hasItem(DEFAULT_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES)))
            .andExpect(jsonPath("$.[*].earliestAppointmentTime").value(hasItem(DEFAULT_EARLIEST_APPOINTMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].latestAppointmentTime").value(hasItem(DEFAULT_LATEST_APPOINTMENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].calendarId").value(hasItem(DEFAULT_CALENDAR_ID.toString())));
    }

    @Test
    @Transactional
    public void getDoctorSchedule() throws Exception {
        // Initialize the database
        doctorScheduleRepository.saveAndFlush(doctorSchedule);

        // Get the doctorSchedule
        restDoctorScheduleMockMvc.perform(get("/api/doctor-schedules/{id}", doctorSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorSchedule.getId().intValue()))
            .andExpect(jsonPath("$.appointmentsDurationMinutes").value(DEFAULT_APPOINTMENTS_DURATION_MINUTES))
            .andExpect(jsonPath("$.intervalBetweenAppointmentsMinutes").value(DEFAULT_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES))
            .andExpect(jsonPath("$.earliestAppointmentTime").value(DEFAULT_EARLIEST_APPOINTMENT_TIME.toString()))
            .andExpect(jsonPath("$.latestAppointmentTime").value(DEFAULT_LATEST_APPOINTMENT_TIME.toString()))
            .andExpect(jsonPath("$.calendarId").value(DEFAULT_CALENDAR_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorSchedule() throws Exception {
        // Get the doctorSchedule
        restDoctorScheduleMockMvc.perform(get("/api/doctor-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorSchedule() throws Exception {
        // Initialize the database
        doctorScheduleService.save(doctorSchedule);

        int databaseSizeBeforeUpdate = doctorScheduleRepository.findAll().size();

        // Update the doctorSchedule
        DoctorSchedule updatedDoctorSchedule = doctorScheduleRepository.findOne(doctorSchedule.getId());
        updatedDoctorSchedule
            .appointmentsDurationMinutes(UPDATED_APPOINTMENTS_DURATION_MINUTES)
            .intervalBetweenAppointmentsMinutes(UPDATED_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES)
            .earliestAppointmentTime(UPDATED_EARLIEST_APPOINTMENT_TIME)
            .latestAppointmentTime(UPDATED_LATEST_APPOINTMENT_TIME)
            .calendarId(UPDATED_CALENDAR_ID);

        restDoctorScheduleMockMvc.perform(put("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoctorSchedule)))
            .andExpect(status().isOk());

        // Validate the DoctorSchedule in the database
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeUpdate);
        DoctorSchedule testDoctorSchedule = doctorScheduleList.get(doctorScheduleList.size() - 1);
        assertThat(testDoctorSchedule.getAppointmentsDurationMinutes()).isEqualTo(UPDATED_APPOINTMENTS_DURATION_MINUTES);
        assertThat(testDoctorSchedule.getIntervalBetweenAppointmentsMinutes()).isEqualTo(UPDATED_INTERVAL_BETWEEN_APPOINTMENTS_MINUTES);
        assertThat(testDoctorSchedule.getEarliestAppointmentTime()).isEqualTo(UPDATED_EARLIEST_APPOINTMENT_TIME);
        assertThat(testDoctorSchedule.getLatestAppointmentTime()).isEqualTo(UPDATED_LATEST_APPOINTMENT_TIME);
        assertThat(testDoctorSchedule.getCalendarId()).isEqualTo(UPDATED_CALENDAR_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorSchedule() throws Exception {
        int databaseSizeBeforeUpdate = doctorScheduleRepository.findAll().size();

        // If the entity doesn't have an ID, there's nothing to update
        restDoctorScheduleMockMvc.perform(put("/api/doctor-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorSchedule isn't in the database
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoctorSchedule() throws Exception {
        // Initialize the database
        doctorScheduleService.save(doctorSchedule);

        int databaseSizeBeforeDelete = doctorScheduleRepository.findAll().size();

        // Get the doctorSchedule
        restDoctorScheduleMockMvc.perform(delete("/api/doctor-schedules/{id}", doctorSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DoctorSchedule> doctorScheduleList = doctorScheduleRepository.findAll();
        assertThat(doctorScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorSchedule.class);
        DoctorSchedule doctorSchedule1 = new DoctorSchedule();
        doctorSchedule1.setId(1L);
        DoctorSchedule doctorSchedule2 = new DoctorSchedule();
        doctorSchedule2.setId(doctorSchedule1.getId());
        assertThat(doctorSchedule1).isEqualTo(doctorSchedule2);
        doctorSchedule2.setId(2L);
        assertThat(doctorSchedule1).isNotEqualTo(doctorSchedule2);
        doctorSchedule1.setId(null);
        assertThat(doctorSchedule1).isNotEqualTo(doctorSchedule2);
    }
}
