package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.PaymentInstallment;
import br.com.tclinica.service.PaymentInstallmentService;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PaymentInstallment.
 */
@RestController
@RequestMapping("/api")
public class PaymentInstallmentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentInstallmentResource.class);

    private static final String ENTITY_NAME = "paymentInstallment";

    private final PaymentInstallmentService paymentInstallmentService;

    public PaymentInstallmentResource(PaymentInstallmentService paymentInstallmentService) {
        this.paymentInstallmentService = paymentInstallmentService;
    }

    /**
     * POST  /payment-installments : Create a new paymentInstallment.
     *
     * @param paymentInstallment the paymentInstallment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentInstallment, or with status 400 (Bad Request) if the paymentInstallment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-installments")
    @Timed
    public ResponseEntity<PaymentInstallment> createPaymentInstallment(@Valid @RequestBody PaymentInstallment paymentInstallment) throws URISyntaxException {
        log.debug("REST request to save PaymentInstallment : {}", paymentInstallment);
        if (paymentInstallment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new paymentInstallment cannot already have an ID")).body(null);
        }
        PaymentInstallment result = paymentInstallmentService.save(paymentInstallment);
        return ResponseEntity.created(new URI("/api/payment-installments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-installments : Updates an existing paymentInstallment.
     *
     * @param paymentInstallment the paymentInstallment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentInstallment,
     * or with status 400 (Bad Request) if the paymentInstallment is not valid,
     * or with status 500 (Internal Server Error) if the paymentInstallment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-installments")
    @Timed
    public ResponseEntity<PaymentInstallment> updatePaymentInstallment(@Valid @RequestBody PaymentInstallment paymentInstallment) throws URISyntaxException {
        log.debug("REST request to update PaymentInstallment : {}", paymentInstallment);
        if (paymentInstallment.getId() == null) {
            return createPaymentInstallment(paymentInstallment);
        }
        PaymentInstallment result = paymentInstallmentService.save(paymentInstallment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentInstallment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-installments : get all the paymentInstallments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentInstallments in body
     */
    @GetMapping("/payment-installments")
    @Timed
    public List<PaymentInstallment> getAllPaymentInstallments() {
        log.debug("REST request to get all PaymentInstallments");
        return paymentInstallmentService.findAll();
        }

    /**
     * GET  /payment-installments/:id : get the "id" paymentInstallment.
     *
     * @param id the id of the paymentInstallment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentInstallment, or with status 404 (Not Found)
     */
    @GetMapping("/payment-installments/{id}")
    @Timed
    public ResponseEntity<PaymentInstallment> getPaymentInstallment(@PathVariable Long id) {
        log.debug("REST request to get PaymentInstallment : {}", id);
        PaymentInstallment paymentInstallment = paymentInstallmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentInstallment));
    }

    /**
     * DELETE  /payment-installments/:id : delete the "id" paymentInstallment.
     *
     * @param id the id of the paymentInstallment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-installments/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentInstallment(@PathVariable Long id) {
        log.debug("REST request to delete PaymentInstallment : {}", id);
        paymentInstallmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
