package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.Prescription;

import br.com.tclinica.repository.PrescriptionRepository;
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
 * REST controller for managing Prescription.
 */
@RestController
@RequestMapping("/api")
public class PrescriptionResource {

    private final Logger log = LoggerFactory.getLogger(PrescriptionResource.class);

    private static final String ENTITY_NAME = "prescription";

    private final PrescriptionRepository prescriptionRepository;
    public PrescriptionResource(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * POST  /prescriptions : Create a new prescription.
     *
     * @param prescription the prescription to create
     * @return the ResponseEntity with status 201 (Created) and with body the new prescription, or with status 400 (Bad Request) if the prescription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/prescriptions")
    @Timed
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to save Prescription : {}", prescription);
        if (prescription.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new prescription cannot already have an ID")).body(null);
        }
        Prescription result = prescriptionRepository.save(prescription);
        return ResponseEntity.created(new URI("/api/prescriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /prescriptions : Updates an existing prescription.
     *
     * @param prescription the prescription to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated prescription,
     * or with status 400 (Bad Request) if the prescription is not valid,
     * or with status 500 (Internal Server Error) if the prescription couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/prescriptions")
    @Timed
    public ResponseEntity<Prescription> updatePrescription(@Valid @RequestBody Prescription prescription) throws URISyntaxException {
        log.debug("REST request to update Prescription : {}", prescription);
        if (prescription.getId() == null) {
            return createPrescription(prescription);
        }
        Prescription result = prescriptionRepository.save(prescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, prescription.getId().toString()))
            .body(result);
    }

    /**
     * GET  /prescriptions : get all the prescriptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of prescriptions in body
     */
    @GetMapping("/prescriptions")
    @Timed
    public List<Prescription> getAllPrescriptions() {
        log.debug("REST request to get all Prescriptions");
        return prescriptionRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /prescriptions/:id : get the "id" prescription.
     *
     * @param id the id of the prescription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the prescription, or with status 404 (Not Found)
     */
    @GetMapping("/prescriptions/{id}")
    @Timed
    public ResponseEntity<Prescription> getPrescription(@PathVariable Long id) {
        log.debug("REST request to get Prescription : {}", id);
        Prescription prescription = prescriptionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(prescription));
    }

    /**
     * DELETE  /prescriptions/:id : delete the "id" prescription.
     *
     * @param id the id of the prescription to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/prescriptions/{id}")
    @Timed
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        log.debug("REST request to delete Prescription : {}", id);
        prescriptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
