package br.com.tclinica.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.tclinica.domain.CardBrand;

import br.com.tclinica.repository.CardBrandRepository;
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
     * PUT  /card-brands : Updates an existing cardBrand.
     *
     * @param cardBrand the cardBrand to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cardBrand,
     * or with status 400 (Bad Request) if the cardBrand is not valid,
     * or with status 500 (Internal Server Error) if the cardBrand couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/card-brands")
    @Timed
    public ResponseEntity<CardBrand> updateCardBrand(@Valid @RequestBody CardBrand cardBrand) throws URISyntaxException {
        log.debug("REST request to update CardBrand : {}", cardBrand);
        if (cardBrand.getId() == null) {
            return createCardBrand(cardBrand);
        }
        CardBrand result = cardBrandRepository.save(cardBrand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cardBrand.getId().toString()))
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
    public ResponseEntity<Void> deleteCardBrand(@PathVariable Long id) {
        log.debug("REST request to delete CardBrand : {}", id);
        cardBrandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
