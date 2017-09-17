package br.com.tclinica.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
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

import br.com.tclinica.domain.Accountant;
import br.com.tclinica.domain.Authority;
import br.com.tclinica.domain.User;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.AccountantService;
import br.com.tclinica.service.UserService;
import br.com.tclinica.service.mapper.UserMapper;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Accountant.
 */
@RestController
@RequestMapping("/api")
public class AccountantResource {

    private final Logger log = LoggerFactory.getLogger(AccountantResource.class);

    private static final String ENTITY_NAME = "accountant";

    private final AccountantService accountantService;
    
    public AccountantResource(AccountantService accountantService) {
        this.accountantService = accountantService;
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
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Accountant> createAccountant(@Valid @RequestBody Accountant accountant) throws URISyntaxException {
        log.debug("REST request to save Accountant : {}", accountant);
        if (accountant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new accountant cannot already have an ID")).body(null);
        }
        Accountant result = accountantService.save(accountant);
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
    @Secured(AuthoritiesConstants.ACCOUNTANT)
    @PreAuthorize("#accountant.user.login == authentication.name")
    public ResponseEntity<Accountant> updateAccountant(@Valid @RequestBody Accountant accountant) throws URISyntaxException {
        log.debug("REST request to update Accountant : {}", accountant);
        if (accountant.getId() == null) {
            return createAccountant(accountant);
        }
        Accountant result = accountantService.save(accountant);
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
    @PostFilter("hasRole('" + AuthoritiesConstants.ADMIN + "') or filterObject.user.login==principal.username")
    public List<Accountant> getAllAccountants() {
        log.debug("REST request to get all Accountants");
        return accountantService.findAll();
        }
 
    /**
     * GET  /accountants/:id : get the "id" accountant.
     *
     * @param id the id of the accountant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accountant, or with status 404 (Not Found)
     */
    @GetMapping("/accountants/{id}")
    @Timed
    @PostAuthorize("hasRole('" + AuthoritiesConstants.ADMIN +"') or returnObject.body.user.login==principal.username")
    public ResponseEntity<Accountant> getAccountant(@PathVariable Long id) {
        log.debug("REST request to get Accountant : {}", id);
        Accountant accountant = accountantService.findOne(id);
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
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAccountant(@PathVariable Long id) {
        log.debug("REST request to delete Accountant : {}", id);
        accountantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
