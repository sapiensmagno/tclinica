package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.ExamType;

import br.com.tclinica.repository.ExamTypeRepository;
import br.com.tclinica.web.rest.util.HeaderUtil;
import br.com.tclinica.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

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
     * POST  /exam-types : Create a new examType.
     *
     * @param examType the examType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examType, or with status 400 (Bad Request) if the examType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exam-types")
    @Timed
    public ResponseEntity<ExamType> createExamType(@Valid @RequestBody ExamType examType) throws URISyntaxException {
        log.debug("REST request to save ExamType : {}", examType);
        if (examType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new examType cannot already have an ID")).body(null);
        }
        ExamType result = examTypeRepository.save(examType);
        return ResponseEntity.created(new URI("/api/exam-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exam-types : Updates an existing examType.
     *
     * @param examType the examType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examType,
     * or with status 400 (Bad Request) if the examType is not valid,
     * or with status 500 (Internal Server Error) if the examType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exam-types")
    @Timed
    public ResponseEntity<ExamType> updateExamType(@Valid @RequestBody ExamType examType) throws URISyntaxException {
        log.debug("REST request to update ExamType : {}", examType);
        if (examType.getId() == null) {
            return createExamType(examType);
        }
        ExamType result = examTypeRepository.save(examType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exam-types : get all the examTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of examTypes in body
     */
    @GetMapping("/exam-types")
    @Timed
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
    public ResponseEntity<Void> deleteExamType(@PathVariable Long id) {
        log.debug("REST request to delete ExamType : {}", id);
        examTypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
