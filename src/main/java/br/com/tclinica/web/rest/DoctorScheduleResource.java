package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.repository.DoctorScheduleRepository;
import br.com.tclinica.service.DoctorScheduleService;
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
 * REST controller for managing DoctorSchedule.
 */
@RestController
@RequestMapping("/api")
public class DoctorScheduleResource {

	private final Logger log = LoggerFactory.getLogger(DoctorScheduleResource.class);

	private static final String ENTITY_NAME = "doctorSchedule";

	private final DoctorScheduleService doctorScheduleService;

	public DoctorScheduleResource(DoctorScheduleService doctorScheduleService) {
		this.doctorScheduleService = doctorScheduleService;
	}

	/**
	 * POST /doctor-schedules : Create a new doctorSchedule.
	 *
	 * @param doctorSchedule
	 *            the doctorSchedule to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         doctorSchedule, or with status 400 (Bad Request) if the
	 *         doctorSchedule has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/doctor-schedules")
	@Timed
	public ResponseEntity<DoctorSchedule> createDoctorSchedule(@Valid @RequestBody DoctorSchedule doctorSchedule)
			throws URISyntaxException {
		log.debug("REST request to save DoctorSchedule : {}", doctorSchedule);
		if (doctorSchedule.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists",
					"A new doctorSchedule cannot already have an ID")).body(null);
		}
		DoctorSchedule result = doctorScheduleService.save(doctorSchedule);
		return ResponseEntity.created(new URI("/api/doctor-schedules/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /doctor-schedules : Updates an existing doctorSchedule.
	 *
	 * @param doctorSchedule
	 *            the doctorSchedule to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         doctorSchedule, or with status 400 (Bad Request) if the
	 *         doctorSchedule is not valid, or with status 500 (Internal Server
	 *         Error) if the doctorSchedule couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/doctor-schedules")
	@Timed
	public ResponseEntity<DoctorSchedule> updateDoctorSchedule(@Valid @RequestBody DoctorSchedule doctorSchedule)
			throws URISyntaxException {
		log.debug("REST request to update DoctorSchedule : {}", doctorSchedule);
		if (doctorSchedule.getId() == null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "nonexistent",
					String.format("Tried to update a nonexistent %s", ENTITY_NAME))).body(null);
		}
		DoctorSchedule result = doctorScheduleService.save(doctorSchedule);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorSchedule.getId().toString()))
				.body(result);
	}

	/**
	 * GET /doctor-schedules : get all the doctorSchedules.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         doctorSchedules in body
	 */
	@GetMapping("/doctor-schedules")
	@Timed
	public List<DoctorSchedule> getAllDoctorSchedules() {
		log.debug("REST request to get all DoctorSchedules");
		return doctorScheduleService.findAll();
	}

	/**
	 * GET /doctor-schedules/:id : get the "id" doctorSchedule.
	 *
	 * @param id
	 *            the id of the doctorSchedule to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         doctorSchedule, or with status 404 (Not Found)
	 */
	@GetMapping("/doctor-schedules/{id}")
	@Timed
	public ResponseEntity<DoctorSchedule> getDoctorSchedule(@PathVariable Long id) {
		log.debug("REST request to get DoctorSchedule : {}", id);
		DoctorSchedule doctorSchedule = doctorScheduleService.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(doctorSchedule));
	}

	/**
	 * DELETE /doctor-schedules/:id : delete the "id" doctorSchedule.
	 *
	 * @param id
	 *            the id of the doctorSchedule to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/doctor-schedules/{id}")
	@Timed
	public ResponseEntity<Void> deleteDoctorSchedule(@PathVariable Long id) {
		log.debug("REST request to delete DoctorSchedule : {}", id);
		doctorScheduleService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}
}
