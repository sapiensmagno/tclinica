package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.Accountant;

import br.com.tclinica.repository.AccountantRepository;
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
 * REST controller for managing Accountant.
 */
@RestController
@RequestMapping("/api")
public class AccountantResource {

    private final Logger log = LoggerFactory.getLogger(AccountantResource.class);

    private static final String ENTITY_NAME = "accountant";

    private final AccountantRepository accountantRepository;
    public AccountantResource(AccountantRepository accountantRepository) {
        this.accountantRepository = accountantRepository;
    }

    /**
     * POST  /accountants : Create a new accountant.
     *
     * @param accountant the accountant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accountant, or with status 400 (Bad Request) if the accountant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/accountants")
    @Timed
    public ResponseEntity<Accountant> createAccountant(@RequestBody Accountant accountant) throws URISyntaxException {
        log.debug("REST request to save Accountant : {}", accountant);
        if (accountant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountant cannot already have an ID")).body(null);
        }
        Accountant result = accountantRepository.save(accountant);
        return ResponseEntity.created(new URI("/api/accountants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accountants : Updates an existing accountant.
     *
     * @param accountant the accountant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accountant,
     * or with status 400 (Bad Request) if the accountant is not valid,
     * or with status 500 (Internal Server Error) if the accountant couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/accountants")
    @Timed
    public ResponseEntity<Accountant> updateAccountant(@RequestBody Accountant accountant) throws URISyntaxException {
        log.debug("REST request to update Accountant : {}", accountant);
        if (accountant.getId() == null) {
            return createAccountant(accountant);
        }
        Accountant result = accountantRepository.save(accountant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, accountant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accountants : get all the accountants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accountants in body
     */
    @GetMapping("/accountants")
    @Timed
    public List<Accountant> getAllAccountants() {
        log.debug("REST request to get all Accountants");
        return accountantRepository.findAll();
        }

    /**
     * GET  /accountants/:id : get the "id" accountant.
     *
     * @param id the id of the accountant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountant, or with status 404 (Not Found)
     */
    @GetMapping("/accountants/{id}")
    @Timed
    public ResponseEntity<Accountant> getAccountant(@PathVariable Long id) {
        log.debug("REST request to get Accountant : {}", id);
        Accountant accountant = accountantRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountant));
    }

    /**
     * DELETE  /accountants/:id : delete the "id" accountant.
     *
     * @param id the id of the accountant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/accountants/{id}")
    @Timed
    public ResponseEntity<Void> deleteAccountant(@PathVariable Long id) {
        log.debug("REST request to delete Accountant : {}", id);
        accountantRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
