package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.PaymentInstallment;
import br.com.tclinica.domain.PaymentMethod;
import br.com.tclinica.repository.PaymentInstallmentRepository;
import br.com.tclinica.service.PaymentInstallmentService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaymentInstallmentResource REST controller.
 *
 * @see PaymentInstallmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TclinicaApp.class)
public class PaymentInstallmentResourceIntTest {

    private static final LocalDate DEFAULT_PAY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_INSTALLMENT_NUMBER = 1;
    private static final Integer UPDATED_INSTALLMENT_NUMBER = 2;

    private static final String DEFAULT_CHECK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_FINAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_FINAL_NUMBER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CANCELLED = false;
    private static final Boolean UPDATED_CANCELLED = true;

    @Autowired
    private PaymentInstallmentRepository paymentInstallmentRepository;

    @Autowired
    private PaymentInstallmentService paymentInstallmentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentInstallmentMockMvc;

    private PaymentInstallment paymentInstallment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentInstallmentResource paymentInstallmentResource = new PaymentInstallmentResource(paymentInstallmentService);
        this.restPaymentInstallmentMockMvc = MockMvcBuilders.standaloneSetup(paymentInstallmentResource)
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
    public static PaymentInstallment createEntity(EntityManager em) {
        PaymentInstallment paymentInstallment = new PaymentInstallment()
            .payDate(DEFAULT_PAY_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .value(DEFAULT_VALUE)
            .installmentNumber(DEFAULT_INSTALLMENT_NUMBER)
            .checkNumber(DEFAULT_CHECK_NUMBER)
            .cardFinalNumber(DEFAULT_CARD_FINAL_NUMBER)
            .cancelled(DEFAULT_CANCELLED);
        // Add required entity
        PaymentMethod paymentMethod = PaymentMethodResourceIntTest.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        paymentInstallment.setPaymentMethod(paymentMethod);
        return paymentInstallment;
    }

    @Before
    public void initTest() {
        paymentInstallment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentInstallment() throws Exception {
        int databaseSizeBeforeCreate = paymentInstallmentRepository.findAll().size();

        // Create the PaymentInstallment
        restPaymentInstallmentMockMvc.perform(post("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentInstallment)))
            .andExpect(status().isCreated());

        // Validate the PaymentInstallment in the database
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentInstallment testPaymentInstallment = paymentInstallmentList.get(paymentInstallmentList.size() - 1);
        assertThat(testPaymentInstallment.getPayDate()).isEqualTo(DEFAULT_PAY_DATE);
        assertThat(testPaymentInstallment.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testPaymentInstallment.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPaymentInstallment.getInstallmentNumber()).isEqualTo(DEFAULT_INSTALLMENT_NUMBER);
        assertThat(testPaymentInstallment.getCheckNumber()).isEqualTo(DEFAULT_CHECK_NUMBER);
        assertThat(testPaymentInstallment.getCardFinalNumber()).isEqualTo(DEFAULT_CARD_FINAL_NUMBER);
        assertThat(testPaymentInstallment.isCancelled()).isEqualTo(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    public void createPaymentInstallmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentInstallmentRepository.findAll().size();

        // Create the PaymentInstallment with an existing ID
        paymentInstallment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentInstallmentMockMvc.perform(post("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentInstallment)))
            .andExpect(status().isBadRequest());

        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPaymentInstallments() throws Exception {
        // Initialize the database
        paymentInstallmentRepository.saveAndFlush(paymentInstallment);

        // Get all the paymentInstallmentList
        restPaymentInstallmentMockMvc.perform(get("/api/payment-installments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentInstallment.getId().intValue())))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].installmentNumber").value(hasItem(DEFAULT_INSTALLMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].checkNumber").value(hasItem(DEFAULT_CHECK_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].cardFinalNumber").value(hasItem(DEFAULT_CARD_FINAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getPaymentInstallment() throws Exception {
        // Initialize the database
        paymentInstallmentRepository.saveAndFlush(paymentInstallment);

        // Get the paymentInstallment
        restPaymentInstallmentMockMvc.perform(get("/api/payment-installments/{id}", paymentInstallment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentInstallment.getId().intValue()))
            .andExpect(jsonPath("$.payDate").value(DEFAULT_PAY_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.installmentNumber").value(DEFAULT_INSTALLMENT_NUMBER))
            .andExpect(jsonPath("$.checkNumber").value(DEFAULT_CHECK_NUMBER.toString()))
            .andExpect(jsonPath("$.cardFinalNumber").value(DEFAULT_CARD_FINAL_NUMBER.toString()))
            .andExpect(jsonPath("$.cancelled").value(DEFAULT_CANCELLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentInstallment() throws Exception {
        // Get the paymentInstallment
        restPaymentInstallmentMockMvc.perform(get("/api/payment-installments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentInstallment() throws Exception {
        // Initialize the database
        paymentInstallmentService.save(paymentInstallment);

        int databaseSizeBeforeUpdate = paymentInstallmentRepository.findAll().size();

        // Update the paymentInstallment
        PaymentInstallment updatedPaymentInstallment = paymentInstallmentRepository.findOne(paymentInstallment.getId());
        updatedPaymentInstallment
            .payDate(UPDATED_PAY_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .value(UPDATED_VALUE)
            .installmentNumber(UPDATED_INSTALLMENT_NUMBER)
            .checkNumber(UPDATED_CHECK_NUMBER)
            .cardFinalNumber(UPDATED_CARD_FINAL_NUMBER)
            .cancelled(UPDATED_CANCELLED);

        restPaymentInstallmentMockMvc.perform(put("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentInstallment)))
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentInstallment() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstallmentRepository.findAll().size();

        restPaymentInstallmentMockMvc.perform(put("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentInstallment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentInstallment in the database
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentInstallment() throws Exception {
        // Initialize the database
        paymentInstallmentService.save(paymentInstallment);

        int databaseSizeBeforeDelete = paymentInstallmentRepository.findAll().size();

        // Get the paymentInstallment
        restPaymentInstallmentMockMvc.perform(delete("/api/payment-installments/{id}", paymentInstallment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
        
        assertThat(paymentInstallment.isCancelled());
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentInstallment.class);
        PaymentInstallment paymentInstallment1 = new PaymentInstallment();
        paymentInstallment1.setId(1L);
        PaymentInstallment paymentInstallment2 = new PaymentInstallment();
        paymentInstallment2.setId(paymentInstallment1.getId());
        assertThat(paymentInstallment1).isEqualTo(paymentInstallment2);
        paymentInstallment2.setId(2L);
        assertThat(paymentInstallment1).isNotEqualTo(paymentInstallment2);
        paymentInstallment1.setId(null);
        assertThat(paymentInstallment1).isNotEqualTo(paymentInstallment2);
    }
}
