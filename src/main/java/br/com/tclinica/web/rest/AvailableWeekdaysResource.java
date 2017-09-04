package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.service.AvailableWeekdaysService;
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
 * REST controller for managing AvailableWeekdays.
 */
@RestController
@RequestMapping("/api")
public class AvailableWeekdaysResource {

    private final Logger log = LoggerFactory.getLogger(AvailableWeekdaysResource.class);

    private static final String ENTITY_NAME = "availableWeekdays";

    private final AvailableWeekdaysService availableWeekdaysService;

    public AvailableWeekdaysResource(AvailableWeekdaysService availableWeekdaysService) {
        this.availableWeekdaysService = availableWeekdaysService;
    }

    /**
     * POST  /available-weekdays : Create a new availableWeekdays.
     *
     * @param availableWeekdays the availableWeekdays to create
     * @return the ResponseEntity with status 201 (Created) and with body the new availableWeekdays, or with status 400 (Bad Request) if the availableWeekdays has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/available-weekdays")
    @Timed
    public ResponseEntity<AvailableWeekdays> createAvailableWeekdays(@Valid @RequestBody AvailableWeekdays availableWeekdays) throws URISyntaxException {
        log.debug("REST request to save AvailableWeekdays : {}", availableWeekdays);
        if (availableWeekdays.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new availableWeekdays cannot already have an ID")).body(null);
        }
        AvailableWeekdays result = availableWeekdaysService.save(availableWeekdays);
        return ResponseEntity.created(new URI("/api/available-weekdays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /available-weekdays : Updates an existing availableWeekdays.
     *
     * @param availableWeekdays the availableWeekdays to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated availableWeekdays,
     * or with status 400 (Bad Request) if the availableWeekdays is not valid,
     * or with status 500 (Internal Server Error) if the availableWeekdays couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/available-weekdays")
    @Timed
    public ResponseEntity<AvailableWeekdays> updateAvailableWeekdays(@Valid @RequestBody AvailableWeekdays availableWeekdays) throws URISyntaxException {
        log.debug("REST request to update AvailableWeekdays : {}", availableWeekdays);
        if (availableWeekdays.getId() == null) {
            return createAvailableWeekdays(availableWeekdays);
        }
        AvailableWeekdays result = availableWeekdaysService.save(availableWeekdays);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, availableWeekdays.getId().toString()))
            .body(result);
    }

    /**
     * GET  /available-weekdays : get all the availableWeekdays.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of availableWeekdays in body
     */
    @GetMapping("/available-weekdays")
    @Timed
    public List<AvailableWeekdays> getAllAvailableWeekdays() {
        log.debug("REST request to get all AvailableWeekdays");
        return availableWeekdaysService.findAll();
        }

    /**
     * GET  /available-weekdays/:id : get the "id" availableWeekdays.
     *
     * @param id the id of the availableWeekdays to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the availableWeekdays, or with status 404 (Not Found)
     */
    @GetMapping("/available-weekdays/{id}")
    @Timed
    public ResponseEntity<AvailableWeekdays> getAvailableWeekdays(@PathVariable Long id) {
        log.debug("REST request to get AvailableWeekdays : {}", id);
        AvailableWeekdays availableWeekdays = availableWeekdaysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(availableWeekdays));
    }

    /**
     * DELETE  /available-weekdays/:id : delete the "id" availableWeekdays.
     *
     * @param id the id of the availableWeekdays to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/available-weekdays/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvailableWeekdays(@PathVariable Long id) {
        log.debug("REST request to delete AvailableWeekdays : {}", id);
        availableWeekdaysService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
