package br.com.tclinica.service.impl;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.repository.DoctorScheduleRepository;
import br.com.tclinica.service.AvailableWeekdaysService;
import br.com.tclinica.service.DoctorScheduleService;
import br.com.tclinica.service.util.ExistenceUtil;

/**
 * Service Implementation for managing DoctorSchedule.
 */
@Service
@Transactional
public class DoctorScheduleServiceImpl implements DoctorScheduleService{

    private final Logger log = LoggerFactory.getLogger(DoctorScheduleServiceImpl.class);

    private final DoctorScheduleRepository doctorScheduleRepository;
    
    private final AvailableWeekdaysService availableWeekdaysService;
    
    public DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository, AvailableWeekdaysService availableWeekdaysService) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.availableWeekdaysService = availableWeekdaysService;
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
        if (ExistenceUtil.entityDoesntExist(doctorSchedule.getId(), doctorScheduleRepository)) {
        	return create(doctorSchedule);
        }
        return doctorScheduleRepository.save(doctorSchedule);
    }
    
    // when creating a new schedule, set default fields and create available weekdays
    public DoctorSchedule create(DoctorSchedule doctorSchedule) {
    	doctorSchedule.setEarliestAppointmentTime(doctorSchedule.getDefaultStartTime());
    	doctorSchedule.setAppointmentsDurationMinutes(DoctorSchedule.getDefaultAppointmentDuration());
    	doctorSchedule.setLatestAppointmentTime(doctorSchedule.getDefaultStartTime().plus(10, ChronoUnit.HOURS));
    	final DoctorSchedule savedSchedule = doctorScheduleRepository.save(doctorSchedule);
    	
    	java.util.Arrays.asList(DayOfWeek.values())
    	.stream()
    	.limit(DayOfWeek.FRIDAY.getValue())
    	.map(day -> availableWeekdaysService.createInstance(day, savedSchedule))
    	.forEach(availableDay -> availableWeekdaysService.save(availableDay));
    	
    	return savedSchedule;
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
