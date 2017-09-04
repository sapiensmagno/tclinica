package br.com.tclinica.service;

import br.com.tclinica.domain.AvailableWeekdays;
import java.util.List;

/**
 * Service Interface for managing AvailableWeekdays.
 */
public interface AvailableWeekdaysService {

    /**
     * Save a availableWeekdays.
     *
     * @param availableWeekdays the entity to save
     * @return the persisted entity
     */
    AvailableWeekdays save(AvailableWeekdays availableWeekdays);

    /**
     *  Get all the availableWeekdays.
     *
     *  @return the list of entities
     */
    List<AvailableWeekdays> findAll();

    /**
     *  Get the "id" availableWeekdays.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AvailableWeekdays findOne(Long id);

    /**
     *  Delete the "id" availableWeekdays.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
