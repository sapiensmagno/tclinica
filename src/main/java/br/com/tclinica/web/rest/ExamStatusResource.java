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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.Exam;
import br.com.tclinica.domain.ExamStatus;
import br.com.tclinica.domain.enumeration.ExamStatuses;
import br.com.tclinica.repository.ExamRepository;
import br.com.tclinica.repository.ExamStatusRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.ExamStatusService;
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
    
    private final ExamStatusService examStatusService;
    
    private final ExamRepository examRepository;
    
    public ExamStatusResource(ExamStatusRepository examStatusRepository, 
    		ExamStatusService examStatusService,
    		ExamRepository examRepository) {
        this.examStatusRepository = examStatusRepository;
        this.examStatusService = examStatusService;
        this.examRepository = examRepository;
    }
    
    @PostMapping("/exam-statuses/labRequest")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<ExamStatus> laboratoryRequest(@RequestBody Exam exam) throws URISyntaxException {
    	log.debug("Create status " + ExamStatuses.LAB_REQUEST +" for exam id: %d", exam.getId());
    	ExamStatus result = examStatusService.create(exam, ExamStatuses.LAB_REQUEST);
    	return ResponseEntity.created(new URI("/api/exam-statuses/" + result.getId()))
              .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
              .body(result);
    }
        
    @PostMapping("/exam-statuses/toDoctor")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<ExamStatus> giveToDoctor(@RequestBody Exam exam) throws URISyntaxException {
    	log.debug("Create status " + ExamStatuses.TO_DOC +" for exam id: %d", exam.getId());
    	ExamStatus result = examStatusService.create(exam, ExamStatuses.TO_DOC);
    	return ResponseEntity.created(new URI("/api/exam-statuses/" + result.getId()))
              .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
              .body(result);
    }
    
    @PostMapping("/exam-statuses/toPatient")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<ExamStatus> giveToPatient(@RequestBody Exam exam) throws URISyntaxException {
    	log.debug("Create status " + ExamStatuses.TO_PATIENT +" for exam id: %d", exam.getId());
    	ExamStatus result = examStatusService.create(exam, ExamStatuses.TO_PATIENT);
    	return ResponseEntity.created(new URI("/api/exam-statuses/" + result.getId()))
              .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
              .body(result);
    }
    
    @PostMapping("/exam-statuses/archive")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<ExamStatus> archive(@RequestBody Exam exam) throws URISyntaxException {
    	log.debug("Create status " + ExamStatuses.ARCHIVE +" for exam id: %d", exam.getId());
    	ExamStatus result = examStatusService.create(exam, ExamStatuses.ARCHIVE);
    	return ResponseEntity.created(new URI("/api/exam-statuses/" + result.getId()))
              .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
              .body(result);
    }
    
    /**
     * GET  /exam-statuses : get all the examStatuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of examStatuses in body
     */
    @GetMapping("/exam-statuses")
    @Timed
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
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
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.DOCTOR + "','" 
    		+ AuthoritiesConstants.RECEPTIONIST + "')")
    public ResponseEntity<ExamStatus> getExamStatus(@PathVariable Long id) {
        log.debug("REST request to get ExamStatus : {}", id);
        ExamStatus examStatus = examStatusRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examStatus));
    }

}
