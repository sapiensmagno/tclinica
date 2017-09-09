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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.tclinica.domain.CardBrand;
import br.com.tclinica.repository.CardBrandRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CardBrand.
 */
@RestController
@RequestMapping("/api")
public class CardBrandResource {

    private final Logger log = LoggerFactory.getLogger(CardBrandResource.class);

    private static final String ENTITY_NAME = "cardBrand";

    private final CardBrandRepository cardBrandRepository;
    public CardBrandResource(CardBrandRepository cardBrandRepository) {
        this.cardBrandRepository = cardBrandRepository;
    }

    /**
     * POST  /card-brands : Create a new cardBrand.
     *
     * @param cardBrand the cardBrand to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cardBrand, or with status 400 (Bad Request) if the cardBrand has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/card-brands")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<CardBrand> createCardBrand(@Valid @RequestBody CardBrand cardBrand) throws URISyntaxException {
        log.debug("REST request to save CardBrand : {}", cardBrand);
        if (cardBrand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cardBrand cannot already have an ID")).body(null);
        }
        CardBrand result = cardBrandRepository.save(cardBrand);
        return ResponseEntity.created(new URI("/api/card-brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /card-brands : get all the cardBrands.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cardBrands in body
     */
    @GetMapping("/card-brands")
    @Timed
    public List<CardBrand> getAllCardBrands() {
        log.debug("REST request to get all CardBrands");
        return cardBrandRepository.findAll();
        }

    /**
     * GET  /card-brands/:id : get the "id" cardBrand.
     *
     * @param id the id of the cardBrand to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cardBrand, or with status 404 (Not Found)
     */
    @GetMapping("/card-brands/{id}")
    @Timed
    public ResponseEntity<CardBrand> getCardBrand(@PathVariable Long id) {
        log.debug("REST request to get CardBrand : {}", id);
        CardBrand cardBrand = cardBrandRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cardBrand));
    }

    /**
     * DELETE  /card-brands/:id : delete the "id" cardBrand.
     *
     * @param id the id of the cardBrand to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/card-brands/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteCardBrand(@PathVariable Long id) {
        log.debug("REST request to delete CardBrand : {}", id);
        cardBrandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
