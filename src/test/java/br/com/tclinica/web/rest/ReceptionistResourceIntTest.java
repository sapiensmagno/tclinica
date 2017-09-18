package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.Receptionist;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.ReceptionistRepository;
import br.com.tclinica.service.ReceptionistService;
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
 * Test class for the ReceptionistResource REST controller.
 *
 * @see ReceptionistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class ReceptionistResourceIntTest {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private ReceptionistService receptionistService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReceptionistMockMvc;

    private Receptionist receptionist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReceptionistResource receptionistResource = new ReceptionistResource(receptionistService);
        this.restReceptionistMockMvc = MockMvcBuilders.standaloneSetup(receptionistResource)
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
    public static Receptionist createEntity(EntityManager em) {
        Receptionist receptionist = new Receptionist()
            .nickname(DEFAULT_NICKNAME)
            .inactive(DEFAULT_INACTIVE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        receptionist.setUser(user);
        return receptionist;
    }

    @Before
    public void initTest() {
        receptionist = createEntity(em);
    }

    @Test
    @Transactional
    public void createReceptionist() throws Exception {
        int databaseSizeBeforeCreate = receptionistRepository.findAll().size();

        // Create the Receptionist
        restReceptionistMockMvc.perform(post("/api/receptionists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receptionist)))
            .andExpect(status().isCreated());

        // Validate the Receptionist in the database
        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeCreate + 1);
        Receptionist testReceptionist = receptionistList.get(receptionistList.size() - 1);
        assertThat(testReceptionist.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testReceptionist.isInactive()).isEqualTo(DEFAULT_INACTIVE);
    }

    @Test
    @Transactional
    public void createReceptionistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = receptionistRepository.findAll().size();

        // Create the Receptionist with an existing ID
        receptionist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionistMockMvc.perform(post("/api/receptionists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receptionist)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInactiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = receptionistRepository.findAll().size();
        // set the field null
        receptionist.setInactive(null);

        // Create the Receptionist, which fails.

        restReceptionistMockMvc.perform(post("/api/receptionists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receptionist)))
            .andExpect(status().isBadRequest());

        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReceptionists() throws Exception {
        // Initialize the database
        receptionistRepository.saveAndFlush(receptionist);

        // Get all the receptionistList
        restReceptionistMockMvc.perform(get("/api/receptionists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionist.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getReceptionist() throws Exception {
        // Initialize the database
        receptionistRepository.saveAndFlush(receptionist);

        // Get the receptionist
        restReceptionistMockMvc.perform(get("/api/receptionists/{id}", receptionist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(receptionist.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReceptionist() throws Exception {
        // Get the receptionist
        restReceptionistMockMvc.perform(get("/api/receptionists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReceptionist() throws Exception {
        // Initialize the database
        receptionistService.save(receptionist);

        int databaseSizeBeforeUpdate = receptionistRepository.findAll().size();

        // Update the receptionist
        Receptionist updatedReceptionist = receptionistRepository.findOne(receptionist.getId());
        updatedReceptionist
            .nickname(UPDATED_NICKNAME)
            .inactive(UPDATED_INACTIVE);

        restReceptionistMockMvc.perform(put("/api/receptionists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReceptionist)))
            .andExpect(status().isOk());

        // Validate the Receptionist in the database
        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeUpdate);
        Receptionist testReceptionist = receptionistList.get(receptionistList.size() - 1);
        assertThat(testReceptionist.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testReceptionist.isInactive()).isEqualTo(UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingReceptionist() throws Exception {
        int databaseSizeBeforeUpdate = receptionistRepository.findAll().size();

        // Create the Receptionist

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restReceptionistMockMvc.perform(put("/api/receptionists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(receptionist)))
            .andExpect(status().isCreated());

        // Validate the Receptionist in the database
        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReceptionist() throws Exception {
        // Initialize the database
        receptionistService.save(receptionist);

        int databaseSizeBeforeDelete = receptionistRepository.findAll().size();

        // Get the receptionist
        restReceptionistMockMvc.perform(delete("/api/receptionists/{id}", receptionist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        assertThat(receptionist.isInactive());        
        List<Receptionist> receptionistList = receptionistRepository.findAll();
        assertThat(receptionistList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Receptionist.class);
        Receptionist receptionist1 = new Receptionist();
        receptionist1.setId(1L);
        Receptionist receptionist2 = new Receptionist();
        receptionist2.setId(receptionist1.getId());
        assertThat(receptionist1).isEqualTo(receptionist2);
        receptionist2.setId(2L);
        assertThat(receptionist1).isNotEqualTo(receptionist2);
        receptionist1.setId(null);
        assertThat(receptionist1).isNotEqualTo(receptionist2);
    }
}
