package br.com.tclinica.service;

import java.util.List;

import br.com.tclinica.domain.Accountant;
import br.com.tclinica.domain.User;

/**
 * Service Interface for managing Accountant.
 */
public interface AccountantService {

    /**
     * Save a doctor.
     *
     * @param doctor the entity to save
     * @return the persisted entity
     */
	Accountant save(Accountant accountant);

    /**
     *  Get all the Accountants.
     *
     *  @return the list of entities
     */
    List<Accountant> findAll();
    /**
     *  Get the "id" Accountant.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Accountant findOne(Long id);

    /**
     *  Delete the "id" Accountant.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
