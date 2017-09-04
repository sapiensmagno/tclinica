package br.com.tclinica.service.impl;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.repository.DoctorScheduleRepository;
import br.com.tclinica.service.DoctorScheduleService;

/**
 * Service Implementation for managing DoctorSchedule.
 */
@Service
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService{

    private final Logger log = LoggerFactory.getLogger(DoctorScheduleServiceImpl.class);

    private final DoctorScheduleRepository doctorScheduleRepository;
    public DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    /**
     * Save a doctorSchedule.
     *
     * @param doctorSchedule the entity to save
     * @return the persisted entity
     */
    @Override
    public DoctorSchedule save(DoctorSchedule doctorSchedule) {
        log.debug("Request to save DoctorSchedule : {}", doctorSchedule);
        if (!scheduleAlreadyExists(doctorSchedule)) {
        	return create(doctorSchedule);
        }
        return doctorScheduleRepository.save(doctorSchedule);
    }
    
    // when creating a new schedule, set default weekdays
    public DoctorSchedule create(DoctorSchedule doctorSchedule) {
    	doctorSchedule.setEarliestAppointmentTime(doctorSchedule.getDefaultStartTime());
    	doctorSchedule.setAppointmentsDurationMinutes(doctorSchedule.getDefaultAppointmentDuration());
    	doctorSchedule.setLatestAppointmentTime(doctorSchedule.getDefaultStartTime().plus(10, ChronoUnit.HOURS));
    	doctorScheduleRepository.save(doctorSchedule);
    	AvailableWeekdays.getWorkingDays().forEach(wd -> wd.setDoctorSchedule(doctorSchedule));
    	return doctorSchedule;
    }
    
    private boolean scheduleAlreadyExists(DoctorSchedule doctorSchedule) {
    	if (doctorSchedule.getId() == null) {
    		return false;
    	}
		DoctorSchedule previousSchedule = doctorScheduleRepository.findOne(doctorSchedule.getId());
		return (previousSchedule != null && previousSchedule.getId() == doctorSchedule.getId());
	}

    /**
     *  Get all the doctorSchedules.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DoctorSchedule> findAll() {
        log.debug("Request to get all DoctorSchedules");
        return doctorScheduleRepository.findAll();
    }

    /**
     *  Get one doctorSchedule by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public DoctorSchedule findOne(Long id) {
        log.debug("Request to get DoctorSchedule : {}", id);
        return doctorScheduleRepository.findOne(id);
    }

    /**
     *  Delete the  doctorSchedule by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorSchedule : {}", id);
        doctorScheduleRepository.delete(id);
    }
}
