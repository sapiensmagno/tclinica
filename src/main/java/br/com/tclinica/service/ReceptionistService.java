package br.com.tclinica.service;

import br.com.tclinica.domain.Receptionist;
import java.util.List;

/**
 * Service Interface for managing Receptionist.
 */
public interface ReceptionistService {

    /**
     * Save a receptionist.
     *
     * @param receptionist the entity to save
     * @return the persisted entity
     */
    Receptionist save(Receptionist receptionist);

    /**
     *  Get all the receptionists.
     *
     *  @return the list of entities
     */
    List<Receptionist> findAll();

    /**
     *  Get the "id" receptionist.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Receptionist findOne(Long id);

    /**
     *  Delete the "id" receptionist.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
