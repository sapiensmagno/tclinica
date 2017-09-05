package br.com.tclinica.service;

import java.time.ZonedDateTime;
import br.com.tclinica.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Appointment.
 */
public interface AppointmentService {

    /**
     * Save a appointment.
     *
     * @param appointment the entity to save
     * @return the persisted entity
     */
    Appointment save(Appointment appointment);

    /**
     *  Get all the appointments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Appointment> findAll(Pageable pageable);
    /**
     *  Get all the AppointmentDTO where MedicalRecord is null.
     *
     *  @return the list of entities
     */
    List<Appointment> findAllWhereMedicalRecordIsNull();

    /**
     *  Get the "id" appointment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Appointment findOne(Long id);

    /**
     *  Delete the "id" appointment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    boolean isValid(Appointment appointment);
    
    ZonedDateTime calculateEnd(Appointment appointment);
}
