package br.com.tclinica.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.PaymentInstallment;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.PaymentInstallmentService;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

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
    @PreAuthorize("#paymentInstallment.appointment.patient.user.login == authentication.name")
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
     * GET  /payment-installments : get all the paymentInstallments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentInstallments in body
     */
    @GetMapping("/payment-installments")
    @Timed
    @PostFilter("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','" + AuthoritiesConstants.ACCOUNTANT + "') or " +
    		"filterObject.appointment.doctorSchedule.doctor.user.login == authentication.name or " +
    		"filterObject.appointment.patient.user.login == authentication.name")
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
    @PostAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','" + AuthoritiesConstants.ACCOUNTANT + "') or " +
    		"returnObject.body.appointment.doctorSchedule.doctor.user.login == authentication.name or " +
    		"returnObject.body.appointment.patient.user.login == authentication.name")
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
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deletePaymentInstallment(@PathVariable Long id) {
        log.debug("REST request to delete PaymentInstallment : {}", id);
        paymentInstallmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
