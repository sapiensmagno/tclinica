package br.com.tclinica.service;

import java.util.List;

import br.com.tclinica.domain.DoctorSchedule;

/**
 * Service Interface for managing DoctorSchedule.
 */
public interface DoctorScheduleService {

    /**
     * Save a doctorSchedule.
     *
     * @param doctorSchedule the entity to save
     * @return the persisted entity
     */
    DoctorSchedule save(DoctorSchedule doctorSchedule);

    /**
     *  Get all the doctorSchedules.
     *
     *  @return the list of entities
     */
    List<DoctorSchedule> findAll();

    /**
     *  Get the "id" doctorSchedule.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DoctorSchedule findOne(Long id);

    /**
     *  Delete the "id" doctorSchedule.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
