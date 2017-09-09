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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.PaymentMethod;
import br.com.tclinica.repository.PaymentMethodRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing PaymentMethod.
 */
@RestController
@RequestMapping("/api")
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private static final String ENTITY_NAME = "paymentMethod";

    private final PaymentMethodRepository paymentMethodRepository;
    public PaymentMethodResource(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * POST  /payment-methods : Create a new paymentMethod.
     *
     * @param paymentMethod the paymentMethod to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentMethod, or with status 400 (Bad Request) if the paymentMethod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-methods")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<PaymentMethod> createPaymentMethod(@Valid @RequestBody PaymentMethod paymentMethod) throws URISyntaxException {
        log.debug("REST request to save PaymentMethod : {}", paymentMethod);
        if (paymentMethod.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new paymentMethod cannot already have an ID")).body(null);
        }
        PaymentMethod result = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.created(new URI("/api/payment-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-methods : get all the paymentMethods.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paymentMethods in body
     */
    @GetMapping("/payment-methods")
    @Timed
    public List<PaymentMethod> getAllPaymentMethods() {
        log.debug("REST request to get all PaymentMethods");
        return paymentMethodRepository.findAll();
        }

    /**
     * GET  /payment-methods/:id : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethod to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentMethod, or with status 404 (Not Found)
     */
    @GetMapping("/payment-methods/{id}")
    @Timed
    public ResponseEntity<PaymentMethod> getPaymentMethod(@PathVariable Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentMethod));
    }

    /**
     * DELETE  /payment-methods/:id : delete the "id" paymentMethod.
     *
     * @param id the id of the paymentMethod to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-methods/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        paymentMethodRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
