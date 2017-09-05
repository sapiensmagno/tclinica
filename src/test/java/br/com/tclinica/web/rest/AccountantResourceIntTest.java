package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.Accountant;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.AccountantRepository;
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
 * Test class for the AccountantResource REST controller.
 *
 * @see AccountantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class AccountantResourceIntTest {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    @Autowired
    private AccountantRepository accountantRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccountantMockMvc;

    private Accountant accountant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountantResource accountantResource = new AccountantResource(accountantRepository);
        this.restAccountantMockMvc = MockMvcBuilders.standaloneSetup(accountantResource)
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
    public static Accountant createEntity(EntityManager em) {
        Accountant accountant = new Accountant()
            .nickname(DEFAULT_NICKNAME)
            .inactive(DEFAULT_INACTIVE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        accountant.setUser(user);
        return accountant;
    }

    @Before
    public void initTest() {
        accountant = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountant() throws Exception {
        int databaseSizeBeforeCreate = accountantRepository.findAll().size();

        // Create the Accountant
        restAccountantMockMvc.perform(post("/api/accountants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountant)))
            .andExpect(status().isCreated());

        // Validate the Accountant in the database
        List<Accountant> accountantList = accountantRepository.findAll();
        assertThat(accountantList).hasSize(databaseSizeBeforeCreate + 1);
        Accountant testAccountant = accountantList.get(accountantList.size() - 1);
        assertThat(testAccountant.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testAccountant.isInactive()).isEqualTo(DEFAULT_INACTIVE);
    }

    @Test
    @Transactional
    public void createAccountantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountantRepository.findAll().size();

        // Create the Accountant with an existing ID
        accountant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountantMockMvc.perform(post("/api/accountants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountant)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Accountant> accountantList = accountantRepository.findAll();
        assertThat(accountantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAccountants() throws Exception {
        // Initialize the database
        accountantRepository.saveAndFlush(accountant);

        // Get all the accountantList
        restAccountantMockMvc.perform(get("/api/accountants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME.toString())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAccountant() throws Exception {
        // Initialize the database
        accountantRepository.saveAndFlush(accountant);

        // Get the accountant
        restAccountantMockMvc.perform(get("/api/accountants/{id}", accountant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountant.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME.toString()))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountant() throws Exception {
        // Get the accountant
        restAccountantMockMvc.perform(get("/api/accountants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountant() throws Exception {
        // Initialize the database
        accountantRepository.saveAndFlush(accountant);
        int databaseSizeBeforeUpdate = accountantRepository.findAll().size();

        // Update the accountant
        Accountant updatedAccountant = accountantRepository.findOne(accountant.getId());
        updatedAccountant
            .nickname(UPDATED_NICKNAME)
            .inactive(UPDATED_INACTIVE);

        restAccountantMockMvc.perform(put("/api/accountants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountant)))
            .andExpect(status().isOk());

        // Validate the Accountant in the database
        List<Accountant> accountantList = accountantRepository.findAll();
        assertThat(accountantList).hasSize(databaseSizeBeforeUpdate);
        Accountant testAccountant = accountantList.get(accountantList.size() - 1);
        assertThat(testAccountant.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testAccountant.isInactive()).isEqualTo(UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountant() throws Exception {
        int databaseSizeBeforeUpdate = accountantRepository.findAll().size();

        // Create the Accountant

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccountantMockMvc.perform(put("/api/accountants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountant)))
            .andExpect(status().isCreated());

        // Validate the Accountant in the database
        List<Accountant> accountantList = accountantRepository.findAll();
        assertThat(accountantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccountant() throws Exception {
        // Initialize the database
        accountantRepository.saveAndFlush(accountant);
        int databaseSizeBeforeDelete = accountantRepository.findAll().size();

        // Get the accountant
        restAccountantMockMvc.perform(delete("/api/accountants/{id}", accountant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Accountant> accountantList = accountantRepository.findAll();
        assertThat(accountantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accountant.class);
        Accountant accountant1 = new Accountant();
        accountant1.setId(1L);
        Accountant accountant2 = new Accountant();
        accountant2.setId(accountant1.getId());
        assertThat(accountant1).isEqualTo(accountant2);
        accountant2.setId(2L);
        assertThat(accountant1).isNotEqualTo(accountant2);
        accountant1.setId(null);
        assertThat(accountant1).isNotEqualTo(accountant2);
    }
}
