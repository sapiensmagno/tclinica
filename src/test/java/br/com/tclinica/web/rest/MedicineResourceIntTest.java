package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.Medicine;
import br.com.tclinica.repository.MedicineRepository;
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
 * Test class for the MedicineResource REST controller.
 *
 * @see MedicineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class MedicineResourceIntTest {

    private static final String DEFAULT_GENERIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INACTIVE = false;
    private static final Boolean UPDATED_INACTIVE = true;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMedicineMockMvc;

    private Medicine medicine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicineResource medicineResource = new MedicineResource(medicineRepository);
        this.restMedicineMockMvc = MockMvcBuilders.standaloneSetup(medicineResource)
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
    public static Medicine createEntity(EntityManager em) {
        Medicine medicine = new Medicine()
            .genericName(DEFAULT_GENERIC_NAME)
            .brandName(DEFAULT_BRAND_NAME)
            .manufacturer(DEFAULT_MANUFACTURER)
            .inactive(DEFAULT_INACTIVE);
        return medicine;
    }

    @Before
    public void initTest() {
        medicine = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicine() throws Exception {
        int databaseSizeBeforeCreate = medicineRepository.findAll().size();

        // Create the Medicine
        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicine)))
            .andExpect(status().isCreated());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate + 1);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getGenericName()).isEqualTo(DEFAULT_GENERIC_NAME);
        assertThat(testMedicine.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testMedicine.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testMedicine.isInactive()).isEqualTo(DEFAULT_INACTIVE);
    }

    @Test
    @Transactional
    public void createMedicineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicineRepository.findAll().size();

        // Create the Medicine with an existing ID
        medicine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicine)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkGenericNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setGenericName(null);

        // Create the Medicine, which fails.

        restMedicineMockMvc.perform(post("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicine)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicines() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList
        restMedicineMockMvc.perform(get("/api/medicines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME.toString())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].inactive").value(hasItem(DEFAULT_INACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get the medicine
        restMedicineMockMvc.perform(get("/api/medicines/{id}", medicine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medicine.getId().intValue()))
            .andExpect(jsonPath("$.genericName").value(DEFAULT_GENERIC_NAME.toString()))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER.toString()))
            .andExpect(jsonPath("$.inactive").value(DEFAULT_INACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMedicine() throws Exception {
        // Get the medicine
        restMedicineMockMvc.perform(get("/api/medicines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Update the medicine
        Medicine updatedMedicine = medicineRepository.findOne(medicine.getId());
        updatedMedicine
            .genericName(UPDATED_GENERIC_NAME)
            .brandName(UPDATED_BRAND_NAME)
            .manufacturer(UPDATED_MANUFACTURER)
            .inactive(UPDATED_INACTIVE);

        restMedicineMockMvc.perform(put("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicine)))
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getGenericName()).isEqualTo(UPDATED_GENERIC_NAME);
        assertThat(testMedicine.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMedicine.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testMedicine.isInactive()).isEqualTo(UPDATED_INACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Create the Medicine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMedicineMockMvc.perform(put("/api/medicines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medicine)))
            .andExpect(status().isCreated());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);
        int databaseSizeBeforeDelete = medicineRepository.findAll().size();

        // Get the medicine
        restMedicineMockMvc.perform(delete("/api/medicines/{id}", medicine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        assertThat(medicine.isInactive());
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicine.class);
        Medicine medicine1 = new Medicine();
        medicine1.setId(1L);
        Medicine medicine2 = new Medicine();
        medicine2.setId(medicine1.getId());
        assertThat(medicine1).isEqualTo(medicine2);
        medicine2.setId(2L);
        assertThat(medicine1).isNotEqualTo(medicine2);
        medicine1.setId(null);
        assertThat(medicine1).isNotEqualTo(medicine2);
    }
}
