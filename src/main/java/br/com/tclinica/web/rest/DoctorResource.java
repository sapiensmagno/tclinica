package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.service.DoctorService;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Doctor.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private static final String ENTITY_NAME = "doctor";

    private final DoctorService doctorService;

    public DoctorResource(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * POST  /doctors : Create a new doctor.
     *
     * @param doctor the doctor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctor, or with status 400 (Bad Request) if the doctor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctors")
    @Timed
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) throws URISyntaxException {
        log.debug("REST request to save Doctor : {}", doctor);
        if (doctor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new doctor cannot already have an ID")).body(null);
        }
        Doctor result = doctorService.save(doctor);
        return ResponseEntity.created(new URI("/api/doctors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctors : Updates an existing doctor.
     *
     * @param doctor the doctor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctor,
     * or with status 400 (Bad Request) if the doctor is not valid,
     * or with status 500 (Internal Server Error) if the doctor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctors")
    @Timed
    public ResponseEntity<Doctor> updateDoctor(@Valid @RequestBody Doctor doctor) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}", doctor);
        if (doctor.getId() == null) {
            return createDoctor(doctor);
        }
        Doctor result = doctorService.save(doctor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctors : get all the doctors.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of doctors in body
     */
    @GetMapping("/doctors")
    @Timed
    public List<Doctor> getAllDoctors(@RequestParam(required = false) String filter) {
        if ("doctorschedule-is-null".equals(filter)) {
            log.debug("REST request to get all Doctors where doctorSchedule is null");
            return doctorService.findAllWhereDoctorScheduleIsNull();
        }
        log.debug("REST request to get all Doctors");
        return doctorService.findAll();
        }

    /**
     * GET  /doctors/:id : get the "id" doctor.
     *
     * @param id the id of the doctor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctor, or with status 404 (Not Found)
     */
    @GetMapping("/doctors/{id}")
    @Timed
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        Doctor doctor = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(doctor));
    }

    /**
     * DELETE  /doctors/:id : delete the "id" doctor.
     *
     * @param id the id of the doctor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctors/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete Doctor : {}", id);
        doctorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
