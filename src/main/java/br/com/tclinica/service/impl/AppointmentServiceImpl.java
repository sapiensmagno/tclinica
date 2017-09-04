package br.com.tclinica.service.impl;

import br.com.tclinica.service.AppointmentService;
import br.com.tclinica.service.util.ExistenceUtil;
import br.com.tclinica.domain.Appointment;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService{

    private final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Save a appointment.
     *
     * @param appointment the entity to save
     * @return the persisted entity
     */
    @Override
    public Appointment save(Appointment appointment) {
        log.debug("Request to save Appointment : {}", appointment);
        if (ExistenceUtil.entityDoesntExist(appointment.getId(), appointmentRepository)) {
        	create(appointment);
        }
        return appointmentRepository.save(appointment);
    }
    
    public Appointment create (Appointment appointment) {
    	return null;
    }
    
    /**
     *  Get all the appointments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> findAll(Pageable pageable) {
        log.debug("Request to get all Appointments");
        return appointmentRepository.findAll(pageable);
    }

    /**
     *  Get one appointment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Appointment findOne(Long id) {
        log.debug("Request to get Appointment : {}", id);
        return appointmentRepository.findOne(id);
    }

    /**
     *  Delete the  appointment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appointment : {}", id);
        appointmentRepository.delete(id);
    }
}
