package br.com.tclinica.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import br.com.tclinica.domain.ExamType;
import br.com.tclinica.repository.ExamTypeRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import br.com.tclinica.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing ExamType.
 */
@RestController
@RequestMapping("/api")
public class ExamTypeResource {

    private final Logger log = LoggerFactory.getLogger(ExamTypeResource.class);

    private static final String ENTITY_NAME = "examType";

    private final ExamTypeRepository examTypeRepository;
    public ExamTypeResource(ExamTypeRepository examTypeRepository) {
        this.examTypeRepository = examTypeRepository;
    }

    /**
     * GET  /exam-types : get all the examTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of examTypes in body
     */
    @GetMapping("/exam-types")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<List<ExamType>> getAllExamTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ExamTypes");
        Page<ExamType> page = examTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exam-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exam-types/:id : get the "id" examType.
     *
     * @param id the id of the examType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examType, or with status 404 (Not Found)
     */
    @GetMapping("/exam-types/{id}")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<ExamType> getExamType(@PathVariable Long id) {
        log.debug("REST request to get ExamType : {}", id);
        ExamType examType = examTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examType));
    }

    /**
     * DELETE  /exam-types/:id : delete the "id" examType.
     *
     * @param id the id of the examType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exam-types/{id}")
    @Timed
    @Secured(AuthoritiesConstants.DOCTOR)
    public ResponseEntity<Void> deleteExamType(@PathVariable Long id) {
        log.debug("REST request to delete ExamType : {}", id);
        examTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
