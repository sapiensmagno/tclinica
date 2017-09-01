package br.com.tclinica.web.rest;

import br.com.tclinica.TclinicaApp;

import br.com.tclinica.domain.PaymentInstallment;
import br.com.tclinica.repository.PaymentInstallmentRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.com.tclinica.web.rest.TestUtil.sameInstant;
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

    private static final ZonedDateTime DEFAULT_PAY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final String DEFAULT_CHECK_NUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECK_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_FINAL = "AAAAAAAAAA";
    private static final String UPDATED_CARD_FINAL = "BBBBBBBBBB";

    private static final String DEFAULT_PAY_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAY_METHOD = "BBBBBBBBBB";

    @Autowired
    private PaymentInstallmentRepository paymentInstallmentRepository;

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
        final PaymentInstallmentResource paymentInstallmentResource = new PaymentInstallmentResource(paymentInstallmentRepository);
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
            .number(DEFAULT_NUMBER)
            .checkNum(DEFAULT_CHECK_NUM)
            .cardType(DEFAULT_CARD_TYPE)
            .cardFinal(DEFAULT_CARD_FINAL)
            .payMethod(DEFAULT_PAY_METHOD);
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
        assertThat(testPaymentInstallment.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPaymentInstallment.getCheckNum()).isEqualTo(DEFAULT_CHECK_NUM);
        assertThat(testPaymentInstallment.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testPaymentInstallment.getCardFinal()).isEqualTo(DEFAULT_CARD_FINAL);
        assertThat(testPaymentInstallment.getPayMethod()).isEqualTo(DEFAULT_PAY_METHOD);
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

        // Validate the Alice in the database
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
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(sameInstant(DEFAULT_PAY_DATE))))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].checkNum").value(hasItem(DEFAULT_CHECK_NUM.toString())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cardFinal").value(hasItem(DEFAULT_CARD_FINAL.toString())))
            .andExpect(jsonPath("$.[*].payMethod").value(hasItem(DEFAULT_PAY_METHOD.toString())));
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
            .andExpect(jsonPath("$.payDate").value(sameInstant(DEFAULT_PAY_DATE)))
            .andExpect(jsonPath("$.dueDate").value(sameInstant(DEFAULT_DUE_DATE)))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.checkNum").value(DEFAULT_CHECK_NUM.toString()))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.cardFinal").value(DEFAULT_CARD_FINAL.toString()))
            .andExpect(jsonPath("$.payMethod").value(DEFAULT_PAY_METHOD.toString()));
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
        paymentInstallmentRepository.saveAndFlush(paymentInstallment);
        int databaseSizeBeforeUpdate = paymentInstallmentRepository.findAll().size();

        // Update the paymentInstallment
        PaymentInstallment updatedPaymentInstallment = paymentInstallmentRepository.findOne(paymentInstallment.getId());
        updatedPaymentInstallment
            .payDate(UPDATED_PAY_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .value(UPDATED_VALUE)
            .number(UPDATED_NUMBER)
            .checkNum(UPDATED_CHECK_NUM)
            .cardType(UPDATED_CARD_TYPE)
            .cardFinal(UPDATED_CARD_FINAL)
            .payMethod(UPDATED_PAY_METHOD);

        restPaymentInstallmentMockMvc.perform(put("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentInstallment)))
            .andExpect(status().isOk());

        // Validate the PaymentInstallment in the database
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeUpdate);
        PaymentInstallment testPaymentInstallment = paymentInstallmentList.get(paymentInstallmentList.size() - 1);
        assertThat(testPaymentInstallment.getPayDate()).isEqualTo(UPDATED_PAY_DATE);
        assertThat(testPaymentInstallment.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testPaymentInstallment.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPaymentInstallment.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPaymentInstallment.getCheckNum()).isEqualTo(UPDATED_CHECK_NUM);
        assertThat(testPaymentInstallment.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testPaymentInstallment.getCardFinal()).isEqualTo(UPDATED_CARD_FINAL);
        assertThat(testPaymentInstallment.getPayMethod()).isEqualTo(UPDATED_PAY_METHOD);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentInstallment() throws Exception {
        int databaseSizeBeforeUpdate = paymentInstallmentRepository.findAll().size();

        // Create the PaymentInstallment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentInstallmentMockMvc.perform(put("/api/payment-installments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentInstallment)))
            .andExpect(status().isCreated());

        // Validate the PaymentInstallment in the database
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentInstallment() throws Exception {
        // Initialize the database
        paymentInstallmentRepository.saveAndFlush(paymentInstallment);
        int databaseSizeBeforeDelete = paymentInstallmentRepository.findAll().size();

        // Get the paymentInstallment
        restPaymentInstallmentMockMvc.perform(delete("/api/payment-installments/{id}", paymentInstallment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentInstallment> paymentInstallmentList = paymentInstallmentRepository.findAll();
        assertThat(paymentInstallmentList).hasSize(databaseSizeBeforeDelete - 1);
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
