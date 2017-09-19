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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.Patient;
import br.com.tclinica.repository.PatientRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Patient.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    private final PatientRepository patientRepository;
    public PatientResource(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * POST  /patients : Create a new patient.
     *
     * @param patient the patient to create
     * @return the ResponseEntity with status 201 (Created) and with body the new patient, or with status 400 (Bad Request) if the patient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/patients")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')"
    		+ " or #patient.user.login == authentication.name")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new patient cannot already have an ID")).body(null);
        }
        if (patient.getNickname() == null) {
        	patient.setNickname(patient.getUser().getFirstName());
        }
        Patient result = patientRepository.save(patient);
        return ResponseEntity.created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /patients : Updates an existing patient.
     *
     * @param patient the patient to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated patient,
     * or with status 400 (Bad Request) if the patient is not valid,
     * or with status 500 (Internal Server Error) if the patient couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/patients")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')"
    		+ " or #patient.user.login == authentication.name")
    public ResponseEntity<Patient> updatePatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patient);
        if (patient.getId() == null) {
            return createPatient(patient);
        }
        Patient result = patientRepository.save(patient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, patient.getId().toString()))
            .body(result);
    }

    /**
     * GET  /patients : get all the patients.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of patients in body
     */
    @GetMapping("/patients")
    @Timed
	@PostFilter("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','"
			+ AuthoritiesConstants.DOCTOR
			+ "','" + AuthoritiesConstants.RECEPTIONIST + "') "
					+ "or filterObject.user.login==principal.username")
	public List<Patient> getAllPatients() {
		log.debug("REST request to get all Patients");
		return patientRepository.findAll();
    }

    /**
     * GET  /patients/:id : get the "id" patient.
     *
     * @param id the id of the patient to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the patient, or with status 404 (Not Found)
     */
    @GetMapping("/patients/{id}")
    @Timed
    @PostAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','"
			+ AuthoritiesConstants.DOCTOR
			+ "','" + AuthoritiesConstants.RECEPTIONIST + "') "
					+ "or returnObject.body.user.login==principal.username")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Patient patient = patientRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(patient));
    }

    /**
     * DELETE  /patients/:id : delete the "id" patient.
     *
     * @param id the id of the patient to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/patients/{id}")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
