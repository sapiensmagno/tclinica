package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.MedicalRecord;

import br.com.tclinica.repository.MedicalRecordRepository;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MedicalRecord.
 */
@RestController
@RequestMapping("/api")
public class MedicalRecordResource {

    private final Logger log = LoggerFactory.getLogger(MedicalRecordResource.class);

    private static final String ENTITY_NAME = "medicalRecord";

    private final MedicalRecordRepository medicalRecordRepository;
    public MedicalRecordResource(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * POST  /medical-records : Create a new medicalRecord.
     *
     * @param medicalRecord the medicalRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicalRecord, or with status 400 (Bad Request) if the medicalRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medical-records")
    @Timed
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws URISyntaxException {
        log.debug("REST request to save MedicalRecord : {}", medicalRecord);
        if (medicalRecord.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new medicalRecord cannot already have an ID")).body(null);
        }
        MedicalRecord result = medicalRecordRepository.save(medicalRecord);
        return ResponseEntity.created(new URI("/api/medical-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medical-records : Updates an existing medicalRecord.
     *
     * @param medicalRecord the medicalRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicalRecord,
     * or with status 400 (Bad Request) if the medicalRecord is not valid,
     * or with status 500 (Internal Server Error) if the medicalRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medical-records")
    @Timed
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws URISyntaxException {
        log.debug("REST request to update MedicalRecord : {}", medicalRecord);
        if (medicalRecord.getId() == null) {
            return createMedicalRecord(medicalRecord);
        }
        MedicalRecord result = medicalRecordRepository.save(medicalRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicalRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medical-records : get all the medicalRecords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of medicalRecords in body
     */
    @GetMapping("/medical-records")
    @Timed
    public List<MedicalRecord> getAllMedicalRecords() {
        log.debug("REST request to get all MedicalRecords");
        return medicalRecordRepository.findAll();
        }

    /**
     * GET  /medical-records/:id : get the "id" medicalRecord.
     *
     * @param id the id of the medicalRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicalRecord, or with status 404 (Not Found)
     */
    @GetMapping("/medical-records/{id}")
    @Timed
    public ResponseEntity<MedicalRecord> getMedicalRecord(@PathVariable Long id) {
        log.debug("REST request to get MedicalRecord : {}", id);
        MedicalRecord medicalRecord = medicalRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicalRecord));
    }

    /**
     * DELETE  /medical-records/:id : delete the "id" medicalRecord.
     *
     * @param id the id of the medicalRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medical-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        log.debug("REST request to delete MedicalRecord : {}", id);
        medicalRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
