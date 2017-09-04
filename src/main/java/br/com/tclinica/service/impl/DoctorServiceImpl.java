package br.com.tclinica.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Authority;
import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.domain.enumeration.Weekdays;
import br.com.tclinica.repository.DoctorRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.service.DoctorScheduleService;
import br.com.tclinica.service.DoctorService;

/**
 * Service Implementation for managing Doctor.
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService{

    private final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;
    
    @Resource
    private DoctorScheduleService doctorScheduleService;
        
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Save a doctor.
     *
     * @param doctor the entity to save
     * @return the persisted entity
     */
    @Override
    public Doctor save(Doctor doctor) {
        log.debug("Request to save Doctor : {}", doctor);
        if (!doctorAlreadyExists(doctor)) {
        	return create(doctor);
        }
        return doctorRepository.save(doctor);
    }
    
    // when creating a new schedule, set default values and create a schedule
    public Doctor create (Doctor doctor) {
    	doctor.getUser().setAuthorities(defineDefaultAuthorities());
    	doctor.setNickname(defineDefaultNickname(doctor));
    	doctor = doctorRepository.save(doctor);
    	
    	DoctorSchedule schedule = new DoctorSchedule();
    	schedule.setDoctor(doctor);
    	doctor.setDoctorSchedule(doctorScheduleService.save(schedule));
    	
    	return doctor;
    }
    
    private String defineDefaultNickname (Doctor doctor) {
    	String nickname = doctor.getNickname();
    	if (nickname == null || nickname == "") {
    		nickname = (String.format("Dr. %s", doctor.getUser().getFirstName()));
    	}
    	return nickname;
    }
    private Set<Authority> defineDefaultAuthorities () {
    	Set<Authority> authorities = new HashSet<>();
		Authority authority = new Authority();
		authority.setName(AuthoritiesConstants.DOCTOR);
		authorities.add(authority);
		return authorities;
    }
    
	private boolean doctorAlreadyExists(Doctor doctor) {
		if (doctor.getId() == null) {
			return false;
		}
		Doctor previousDoctor = doctorRepository.findOne(doctor.getId());
		return (previousDoctor != null && previousDoctor.getId() == doctor.getId());
	}

    /**
     *  Get all the doctors.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Doctor> findAll() {
        log.debug("Request to get all Doctors");
        return doctorRepository.findAll();
    }


    /**
     *  get all the doctors where DoctorSchedule is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Doctor> findAllWhereDoctorScheduleIsNull() {
        log.debug("Request to get all doctors where DoctorSchedule is null");
        return StreamSupport
            .stream(doctorRepository.findAll().spliterator(), false)
            .filter(doctor -> doctor.getDoctorSchedule() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one doctor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Doctor findOne(Long id) {
        log.debug("Request to get Doctor : {}", id);
        return doctorRepository.findOne(id);
    }

    /**
     *  Delete the  doctor by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Doctor : {}", id);
        doctorRepository.delete(id);
    }
}
