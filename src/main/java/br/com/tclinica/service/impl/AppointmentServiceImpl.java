package br.com.tclinica.service.impl;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Appointment;
import br.com.tclinica.repository.AppointmentRepository;
import br.com.tclinica.service.AppointmentService;
import br.com.tclinica.service.DoctorScheduleService;


/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService{

    private final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleService doctorScheduleService;
    
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorScheduleService doctorScheduleService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleService = doctorScheduleService;
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
        return appointmentRepository.save(appointment);
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
        log.debug("Request to delete Appointment : {}. Cancelling it.", id);
        Appointment appointment = findOne(id);
        appointment.setCancelled(true);
        appointmentRepository.save(appointment);
    }
    
    @Override
    public boolean isValid(Appointment appointment) {
    	// Fail fast: using short-circuit to avoid trying all scenarios
    	boolean valid;
    	Set<Appointment> existingAppointments;
    	Predicate<Appointment> appointmentsDontCrash;    	
    	// Does start and end make sense?
    	valid = appointment.getStartDate().isBefore(appointment.getEndDate());
    	// Is it too early?
    	LocalTime appointmentStartTime = appointment.getStartDate().toLocalTime();
    	LocalTime earliestAppointmentPossible = LocalTime.from(appointment.getDoctorSchedule().getEarliestAppointmentTime().atZone(ZoneId.systemDefault()));
    	valid = valid && appointmentStartTime.isAfter(earliestAppointmentPossible);
    	// Is it too late?
    	LocalTime appointmentEndTime = appointment.getEndDate().toLocalTime();
    	LocalTime latestAppointmentPossible = LocalTime.from(appointment.getDoctorSchedule().getLatestAppointmentTime().atZone(ZoneId.systemDefault()));
    	valid = valid && appointmentEndTime.isBefore(latestAppointmentPossible);
    	// TODO Is it an available weekday? (eliminate Weekdays and work with java's DayOfWeek)
    	// Load doctor schedule to get its appointments
    	appointment.setDoctorSchedule(doctorScheduleService.findOne(appointment.getDoctorSchedule().getId()));
    	existingAppointments = appointment.getDoctorSchedule().getAppointments();
    	// Is there a crash?
    	appointmentsDontCrash = existingAppointment ->
			(appointment.getStartDate().isAfter(existingAppointment.getEndDate()) || 
			appointment.getStartDate().isBefore(existingAppointment.getStartDate()))
			&&
			(appointment.getEndDate().isBefore(existingAppointment.getStartDate()) ||
			appointment.getEndDate().isAfter(existingAppointment.getEndDate()));
    	valid = valid && 
    			(existingAppointments.isEmpty() ||
    			existingAppointments.stream()
    			.filter(ap -> !ap.isCancelled())
    			.allMatch(appointmentsDontCrash));
    	return valid;
    }
    
    @Override
    public ZonedDateTime calculateEnd (Appointment appointment) {
    	return appointment.getStartDate().plusMinutes(appointment.getDoctorSchedule().getAppointmentsDurationMinutes());
    }
}
