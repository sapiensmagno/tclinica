package br.com.tclinica.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

import br.com.tclinica.domain.Healthcare;
import br.com.tclinica.repository.HealthcareRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Healthcare.
 */
@RestController
@RequestMapping("/api")
public class HealthcareResource {

    private final Logger log = LoggerFactory.getLogger(HealthcareResource.class);

    private static final String ENTITY_NAME = "healthcare";

    private final HealthcareRepository healthcareRepository;
    public HealthcareResource(HealthcareRepository healthcareRepository) {
        this.healthcareRepository = healthcareRepository;
    }

    /**
     * POST  /healthcares : Create a new healthcare.
     *
     * @param healthcare the healthcare to create
     * @return the ResponseEntity with status 201 (Created) and with body the new healthcare, or with status 400 (Bad Request) if the healthcare has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/healthcares")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Healthcare> createHealthcare(@RequestBody Healthcare healthcare) throws URISyntaxException {
        log.debug("REST request to save Healthcare : {}", healthcare);
        if (healthcare.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new healthcare cannot already have an ID")).body(null);
        }
        Healthcare result = healthcareRepository.save(healthcare);
        return ResponseEntity.created(new URI("/api/healthcares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /healthcares : get all the healthcares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of healthcares in body
     */
    @GetMapping("/healthcares")
    @Timed
    public List<Healthcare> getAllHealthcares() {
        log.debug("REST request to get all Healthcares");
        return healthcareRepository.findAll();
        }

    /**
     * GET  /healthcares/:id : get the "id" healthcare.
     *
     * @param id the id of the healthcare to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the healthcare, or with status 404 (Not Found)
     */
    @GetMapping("/healthcares/{id}")
    @Timed
    public ResponseEntity<Healthcare> getHealthcare(@PathVariable Long id) {
        log.debug("REST request to get Healthcare : {}", id);
        Healthcare healthcare = healthcareRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(healthcare));
    }

    /**
     * DELETE  /healthcares/:id : delete the "id" healthcare.
     *
     * @param id the id of the healthcare to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/healthcares/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteHealthcare(@PathVariable Long id) {
        log.debug("REST request to delete Healthcare : {}", id);
        healthcareRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
