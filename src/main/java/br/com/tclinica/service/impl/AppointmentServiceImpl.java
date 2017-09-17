package br.com.tclinica.service.impl;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Appointment;
import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.Patient;
import br.com.tclinica.domain.User;
import br.com.tclinica.repository.AppointmentRepository;
import br.com.tclinica.repository.PatientRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.security.SecurityUtils;
import br.com.tclinica.service.AppointmentService;
import br.com.tclinica.service.AvailableWeekdaysService;
import br.com.tclinica.service.DoctorService;
import br.com.tclinica.service.UserService;

/**
 * Service Implementation for managing Appointment.
 */
@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService{

    private final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final AvailableWeekdaysService availableWeekdaysService;
    
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, 
    		DoctorService doctorService, 
    		PatientRepository patientRepository,
    		AvailableWeekdaysService availableWeekdaysService,
    		UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.userService = userService;
        this.patientRepository = patientRepository;
        this.availableWeekdaysService = availableWeekdaysService;
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
        Appointment saved = appointment;
        Appointment existingAppointment = null;
        if (appointment.getId() != null) {
        	existingAppointment = this.findOne(appointment.getId());
        }
        if (existingAppointment != null && existingAppointment.isCancelled()) {
        	return saved;
        }
        if (appointment.getStartDate().isAfter(ZonedDateTime.now())) {
        	saved= appointmentRepository.save(appointment);
        }
        return saved;
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
        if (allowListAllAppointments()) {
        	return appointmentRepository.findAll(pageable);
        } else if (allowListOnlyDoctorsOwnAppointments()) {
        	User user = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).get();
        	Doctor doctor = doctorService.findByUser(user);
        	return appointmentRepository.findByDoctorSchedule(doctor.getDoctorSchedule(), pageable);
        } else {
        	User user = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).get();
        	Patient patient = patientRepository.findByUser(user);
        	return appointmentRepository.findByPatient(patient, pageable);
        }
    }
    
    @Override
    public boolean allowListAllAppointments() {
    	return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
    }
    
    @Override
    public boolean allowListOnlyDoctorsOwnAppointments () {
    	return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.DOCTOR);
    }


    /**
     *  get all the appointments where MedicalRecord is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Appointment> findAllWhereMedicalRecordIsNull() {
        log.debug("Request to get all appointments where MedicalRecord is null");
        return StreamSupport
            .stream(appointmentRepository.findAll().spliterator(), false)
            .filter(appointment -> appointment.getMedicalRecord() == null)
            .collect(Collectors.toList());
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
        log.debug("Request to delete Doctor : {}", id);
        Appointment appointment = appointmentRepository.findOne(id);
        appointment.setCancelled(true);
        appointmentRepository.save(appointment);
    }
    
    @Override
	public boolean isDeletable(Long id) {
		Appointment appointment = this.findOne(id);
		return !appointment.isCancelled() && appointment.getStartDate().isAfter(ZonedDateTime.now()) && hasDeletePermission(appointment);
	}
    
    private boolean hasDeletePermission (Appointment appointment) {
    	return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
			SecurityUtils.getCurrentUserLogin().equals(appointment.getPatient().getUser().getLogin()) ||
			(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.DOCTOR) &&
			SecurityUtils.getCurrentUserLogin().equals(appointment.getDoctorSchedule().getDoctor().getUser().getLogin()));
    }
    
    @Override
    public boolean isValid(Appointment appointment) {
    	// Fail fast: using short-circuit to avoid trying all scenarios
    	boolean valid;
    	List<Appointment> existingAppointments;
    	Predicate<Appointment> appointmentsDontCrash;    	
    	// Does start and end make sense?
    	valid = appointment.getStartDate().isBefore(appointment.getEndDate());
    	// Is it too early?
    	LocalTime appointmentStartTime = appointment.getStartDate().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
    	LocalTime earliestAppointmentPossible = LocalTime.from(appointment.getDoctorSchedule().getEarliestAppointmentTime().atZone(ZoneId.systemDefault()));
    	valid = valid && appointmentStartTime.isAfter(earliestAppointmentPossible);
    	// Is it too late?
    	LocalTime appointmentEndTime = appointment.getEndDate().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
    	LocalTime latestAppointmentPossible = LocalTime.from(appointment.getDoctorSchedule().getLatestAppointmentTime().atZone(ZoneId.systemDefault()));
    	valid = valid && appointmentEndTime.isBefore(latestAppointmentPossible);
    	//Is it an available weekday?
    	if (valid) {
	    	List<AvailableWeekdays> weekdays = availableWeekdaysService.findByDoctorSchedule(appointment.getDoctorSchedule());
	    	valid = valid && weekdays.stream().anyMatch(wkday -> wkday.getWeekday().equals(appointment.getStartDate().getDayOfWeek()));
    	}
    	if (valid) {
	    	// Load appointments in this schedule
	    	existingAppointments = appointmentRepository.findByDoctorSchedule(appointment.getDoctorSchedule());
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
	    			.filter(existingAppointment -> !existingAppointment.equals(appointment) && !existingAppointment.isCancelled())
	    			.allMatch(appointmentsDontCrash));
    	}
    	return valid;
    }
    
    @Override
    public ZonedDateTime calculateEnd (Appointment appointment) {
    	return appointment.getStartDate().plusMinutes(appointment.getDoctorSchedule().getAppointmentsDurationMinutes());
    }
}
