package br.com.tclinica.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.tclinica.domain.Appointment;

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

	boolean isDeletable(Long id);

	boolean allowListAllAppointments();

	boolean allowListOnlyDoctorsOwnAppointments();
}
