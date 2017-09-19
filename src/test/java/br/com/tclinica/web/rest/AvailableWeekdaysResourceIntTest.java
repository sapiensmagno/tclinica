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

import java.time.DayOfWeek;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.repository.AvailableWeekdaysRepository;
import br.com.tclinica.service.AvailableWeekdaysService;
import br.com.tclinica.service.impl.AvailableWeekdaysServiceImpl;
import br.com.tclinica.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the AvailableWeekdaysResource REST controller.
 *
 * @see AvailableWeekdaysResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class AvailableWeekdaysResourceIntTest {

    private static final DayOfWeek DEFAULT_WEEKDAY = DayOfWeek.of(1);
    private static final DayOfWeek UPDATED_WEEKDAY = DayOfWeek.of(2);

    @Autowired
    private AvailableWeekdaysRepository availableWeekdaysRepository;

    @Autowired 
    private AvailableWeekdaysService availableWeekdaysService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAvailableWeekdaysMockMvc;
    
    private MockMvc spiedRestAvailableWeekdaysMockMvc;

    private AvailableWeekdays availableWeekdays;

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	createResourceWithSpiedService();
        final AvailableWeekdaysResource availableWeekdaysResource = new AvailableWeekdaysResource(availableWeekdaysService);
        this.restAvailableWeekdaysMockMvc = MockMvcBuilders.standaloneSetup(availableWeekdaysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }
    
    private void createResourceWithSpiedService() {
    	AvailableWeekdaysServiceImpl serviceImpl = new AvailableWeekdaysServiceImpl(this.availableWeekdaysRepository);
    	AvailableWeekdaysService spiedAvailableWeekdaysService = Mockito.spy(serviceImpl);
        Mockito.doReturn(true).when(spiedAvailableWeekdaysService).isDeletable(Mockito.anyLong());
        final AvailableWeekdaysResource spiedAvailableWeekdaysResource = new AvailableWeekdaysResource(spiedAvailableWeekdaysService);
        this.spiedRestAvailableWeekdaysMockMvc = MockMvcBuilders.standaloneSetup(spiedAvailableWeekdaysResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AvailableWeekdays createEntity(EntityManager em) {
        AvailableWeekdays availableWeekdays = new AvailableWeekdays()
            .weekday(DEFAULT_WEEKDAY);
        return availableWeekdays;
    }

    @Before
    public void initTest() {
        availableWeekdays = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvailableWeekdays() throws Exception {
        int databaseSizeBeforeCreate = availableWeekdaysRepository.findAll().size();

        // Create the AvailableWeekdays
        restAvailableWeekdaysMockMvc.perform(post("/api/available-weekdays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availableWeekdays)))
            .andExpect(status().isCreated());

        // Validate the AvailableWeekdays in the database
        List<AvailableWeekdays> availableWeekdaysList = availableWeekdaysRepository.findAll();
        assertThat(availableWeekdaysList).hasSize(databaseSizeBeforeCreate + 1);
        AvailableWeekdays testAvailableWeekdays = availableWeekdaysList.get(availableWeekdaysList.size() - 1);
        assertThat(testAvailableWeekdays.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);
    }

    @Test
    @Transactional
    public void createAvailableWeekdaysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = availableWeekdaysRepository.findAll().size();

        // Create the AvailableWeekdays with an existing ID
        availableWeekdays.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailableWeekdaysMockMvc.perform(post("/api/available-weekdays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availableWeekdays)))
            .andExpect(status().isBadRequest());

        List<AvailableWeekdays> availableWeekdaysList = availableWeekdaysRepository.findAll();
        assertThat(availableWeekdaysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWeekdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableWeekdaysRepository.findAll().size();
        // set the field null
        availableWeekdays.setWeekday(null);

        // Create the AvailableWeekdays, which fails.

        restAvailableWeekdaysMockMvc.perform(post("/api/available-weekdays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availableWeekdays)))
            .andExpect(status().isBadRequest());

        List<AvailableWeekdays> availableWeekdaysList = availableWeekdaysRepository.findAll();
        assertThat(availableWeekdaysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAvailableWeekdays() throws Exception {
        // Initialize the database
        availableWeekdaysRepository.saveAndFlush(availableWeekdays);

        // Get all the availableWeekdaysList
        restAvailableWeekdaysMockMvc.perform(get("/api/available-weekdays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availableWeekdays.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())));
    }

    @Test
    @Transactional
    public void getAvailableWeekdays() throws Exception {
        // Initialize the database
        availableWeekdaysRepository.saveAndFlush(availableWeekdays);

        // Get the availableWeekdays
        restAvailableWeekdaysMockMvc.perform(get("/api/available-weekdays/{id}", availableWeekdays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(availableWeekdays.getId().intValue()))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvailableWeekdays() throws Exception {
        // Get the availableWeekdays
        restAvailableWeekdaysMockMvc.perform(get("/api/available-weekdays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailableWeekdays() throws Exception {
        // Initialize the database
        availableWeekdaysService.save(availableWeekdays);

        AvailableWeekdays updatedAvailableWeekdays = availableWeekdaysRepository.findOne(availableWeekdays.getId());
        updatedAvailableWeekdays
            .weekday(UPDATED_WEEKDAY);

        restAvailableWeekdaysMockMvc.perform(put("/api/available-weekdays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvailableWeekdays)))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Transactional
    public void updateNonExistingAvailableWeekdays() throws Exception {
        int databaseSizeBeforeUpdate = availableWeekdaysRepository.findAll().size();

        restAvailableWeekdaysMockMvc.perform(put("/api/available-weekdays")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availableWeekdays)))
            .andExpect(status().isMethodNotAllowed());

        List<AvailableWeekdays> availableWeekdaysList = availableWeekdaysRepository.findAll();
        assertThat(availableWeekdaysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAvailableWeekdays() throws Exception {
        // Initialize the database
    	availableWeekdaysService.save(availableWeekdays);

        int databaseSizeBeforeDelete = availableWeekdaysRepository.findAll().size();

        // Get the availableWeekdays
        spiedRestAvailableWeekdaysMockMvc.perform(delete("/api/available-weekdays/{id}", availableWeekdays.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AvailableWeekdays> availableWeekdaysList = availableWeekdaysRepository.findAll();
        assertThat(availableWeekdaysList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AvailableWeekdays.class);
        AvailableWeekdays availableWeekdays1 = new AvailableWeekdays();
        availableWeekdays1.setId(1L);
        AvailableWeekdays availableWeekdays2 = new AvailableWeekdays();
        availableWeekdays2.setId(availableWeekdays1.getId());
        assertThat(availableWeekdays1).isEqualTo(availableWeekdays2);
        availableWeekdays2.setId(2L);
        assertThat(availableWeekdays1).isNotEqualTo(availableWeekdays2);
        availableWeekdays1.setId(null);
        assertThat(availableWeekdays1).isNotEqualTo(availableWeekdays2);
    }
}
