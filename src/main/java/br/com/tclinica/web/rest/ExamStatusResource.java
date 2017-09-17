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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.repository.ExamStatusRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing ExamStatus.
 */
@RestController
@RequestMapping("/api")
public class ExamStatusResource {

    private final Logger log = LoggerFactory.getLogger(ExamStatusResource.class);

    private static final String ENTITY_NAME = "examStatus";

    private final ExamStatusRepository examStatusRepository;
    public ExamStatusResource(ExamStatusRepository examStatusRepository) {
        this.examStatusRepository = examStatusRepository;
    }

    /**
     * POST  /exam-statuses : Create a new examStatus.
     *
     * @param examStatus the examStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examStatus, or with status 400 (Bad Request) if the examStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exam-statuses")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<ExamStatus> createExamStatus(@Valid @RequestBody ExamStatus examStatus) throws URISyntaxException {
        log.debug("REST request to save ExamStatus : {}", examStatus);
        if (examStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new examStatus cannot already have an ID")).body(null);
        }
        ExamStatus result = examStatusRepository.save(examStatus);
        return ResponseEntity.created(new URI("/api/exam-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exam-statuses : Updates an existing examStatus.
     *
     * @param examStatus the examStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examStatus,
     * or with status 400 (Bad Request) if the examStatus is not valid,
     * or with status 500 (Internal Server Error) if the examStatus couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exam-statuses")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<ExamStatus> updateExamStatus(@Valid @RequestBody ExamStatus examStatus) throws URISyntaxException {
        log.debug("REST request to update ExamStatus : {}", examStatus);
        if (examStatus.getId() == null) {
            return createExamStatus(examStatus);
        }
        ExamStatus result = examStatusRepository.save(examStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exam-statuses : get all the examStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of examStatuses in body
     */
    @GetMapping("/exam-statuses")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public List<ExamStatus> getAllExamStatuses() {
        log.debug("REST request to get all ExamStatuses");
        return examStatusRepository.findAll();
        }

    /**
     * GET  /exam-statuses/:id : get the "id" examStatus.
     *
     * @param id the id of the examStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examStatus, or with status 404 (Not Found)
     */
    @GetMapping("/exam-statuses/{id}")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<ExamStatus> getExamStatus(@PathVariable Long id) {
        log.debug("REST request to get ExamStatus : {}", id);
        ExamStatus examStatus = examStatusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examStatus));
    }

    /**
     * DELETE  /exam-statuses/:id : delete the "id" examStatus.
     *
     * @param id the id of the examStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exam-statuses/{id}")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<Void> deleteExamStatus(@PathVariable Long id) {
        log.debug("REST request to delete ExamStatus : {}", id);
        examStatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
