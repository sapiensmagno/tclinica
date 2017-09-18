package br.com.tclinica.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import br.com.tclinica.domain.Exam;
import br.com.tclinica.domain.enumeration.ExamStatuses;
import br.com.tclinica.repository.ExamRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.ExamStatusService;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Exam.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    private final ExamRepository examRepository;
    
    public ExamResource(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * POST  /exams : Create a new exam.
     *
     * @param exam the exam to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exam, or with status 400 (Bad Request) if the exam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exams")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", exam);
        if (exam.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new exam cannot already have an ID")).body(null);
        }
        Exam result = examRepository.save(exam);
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /exams : Updates an existing exam.
     *
     * @param exam the exam to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exam,
     * or with status 400 (Bad Request) if the exam is not valid,
     * or with status 500 (Internal Server Error) if the exam couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exams")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", exam);
        if (exam.getId() == null) {
            return createExam(exam);
        }
        Exam result = examRepository.save(exam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exam.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exams : get all the exams.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of exams in body
     */
    @GetMapping("/exams")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public List<Exam> getAllExams() {
        log.debug("REST request to get all Exams");
        return examRepository.findAll();
        }

    /**
     * GET  /exams/:id : get the "id" exam.
     *
     * @param id the id of the exam to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exam, or with status 404 (Not Found)
     */
    @GetMapping("/exams/{id}")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<Exam> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        Exam exam = examRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(exam));
    }

    /**
     * DELETE  /exams/:id : delete the "id" exam.
     *
     * @param id the id of the exam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exams/{id}")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
