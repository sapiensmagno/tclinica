package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.CardBrand;
import br.com.tclinica.repository.CardBrandRepository;
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
 * Test class for the CardBrandResource REST controller.
 *
 * @see CardBrandResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class CardBrandResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CardBrandRepository cardBrandRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardBrandMockMvc;

    private CardBrand cardBrand;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CardBrandResource cardBrandResource = new CardBrandResource(cardBrandRepository);
        this.restCardBrandMockMvc = MockMvcBuilders.standaloneSetup(cardBrandResource)
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
    public static CardBrand createEntity(EntityManager em) {
        CardBrand cardBrand = new CardBrand()
            .name(DEFAULT_NAME);
        return cardBrand;
    }

    @Before
    public void initTest() {
        cardBrand = createEntity(em);
    }

    @Test
    @Transactional
    public void createCardBrand() throws Exception {
        int databaseSizeBeforeCreate = cardBrandRepository.findAll().size();

        // Create the CardBrand
        restCardBrandMockMvc.perform(post("/api/card-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardBrand)))
            .andExpect(status().isCreated());

        // Validate the CardBrand in the database
        List<CardBrand> cardBrandList = cardBrandRepository.findAll();
        assertThat(cardBrandList).hasSize(databaseSizeBeforeCreate + 1);
        CardBrand testCardBrand = cardBrandList.get(cardBrandList.size() - 1);
        assertThat(testCardBrand.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCardBrandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardBrandRepository.findAll().size();

        // Create the CardBrand with an existing ID
        cardBrand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardBrandMockMvc.perform(post("/api/card-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardBrand)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CardBrand> cardBrandList = cardBrandRepository.findAll();
        assertThat(cardBrandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardBrandRepository.findAll().size();
        // set the field null
        cardBrand.setName(null);

        // Create the CardBrand, which fails.

        restCardBrandMockMvc.perform(post("/api/card-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardBrand)))
            .andExpect(status().isBadRequest());

        List<CardBrand> cardBrandList = cardBrandRepository.findAll();
        assertThat(cardBrandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCardBrands() throws Exception {
        // Initialize the database
        cardBrandRepository.saveAndFlush(cardBrand);

        // Get all the cardBrandList
        restCardBrandMockMvc.perform(get("/api/card-brands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCardBrand() throws Exception {
        // Initialize the database
        cardBrandRepository.saveAndFlush(cardBrand);

        // Get the cardBrand
        restCardBrandMockMvc.perform(get("/api/card-brands/{id}", cardBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cardBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCardBrand() throws Exception {
        // Get the cardBrand
        restCardBrandMockMvc.perform(get("/api/card-brands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCardBrand() throws Exception {
        // Initialize the database
        cardBrandRepository.saveAndFlush(cardBrand);
        int databaseSizeBeforeUpdate = cardBrandRepository.findAll().size();

        // Update the cardBrand
        CardBrand updatedCardBrand = cardBrandRepository.findOne(cardBrand.getId());
        updatedCardBrand
            .name(UPDATED_NAME);

        restCardBrandMockMvc.perform(put("/api/card-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCardBrand)))
            .andExpect(status().isMethodNotAllowed());

    }

    @Test
    @Transactional
    public void updateNonExistingCardBrand() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandRepository.findAll().size();

        // Create the CardBrand

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardBrandMockMvc.perform(put("/api/card-brands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cardBrand)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardBrand in the database
        List<CardBrand> cardBrandList = cardBrandRepository.findAll();
        assertThat(cardBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCardBrand() throws Exception {
        // Initialize the database
        cardBrandRepository.saveAndFlush(cardBrand);
        int databaseSizeBeforeDelete = cardBrandRepository.findAll().size();

        // Get the cardBrand
        restCardBrandMockMvc.perform(delete("/api/card-brands/{id}", cardBrand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CardBrand> cardBrandList = cardBrandRepository.findAll();
        assertThat(cardBrandList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardBrand.class);
        CardBrand cardBrand1 = new CardBrand();
        cardBrand1.setId(1L);
        CardBrand cardBrand2 = new CardBrand();
        cardBrand2.setId(cardBrand1.getId());
        assertThat(cardBrand1).isEqualTo(cardBrand2);
        cardBrand2.setId(2L);
        assertThat(cardBrand1).isNotEqualTo(cardBrand2);
        cardBrand1.setId(null);
        assertThat(cardBrand1).isNotEqualTo(cardBrand2);
    }
}
