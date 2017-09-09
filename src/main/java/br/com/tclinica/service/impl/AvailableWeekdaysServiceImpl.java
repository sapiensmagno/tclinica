package br.com.tclinica.service.impl;

import java.time.DayOfWeek;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.repository.AvailableWeekdaysRepository;
import br.com.tclinica.security.AuthoritiesConstants;
import br.com.tclinica.security.SecurityUtils;
import br.com.tclinica.service.AvailableWeekdaysService;

/**
 * Service Implementation for managing AvailableWeekdays.
 */
@Service
@Transactional
public class AvailableWeekdaysServiceImpl implements AvailableWeekdaysService{

    private final Logger log = LoggerFactory.getLogger(AvailableWeekdaysServiceImpl.class);

    private final AvailableWeekdaysRepository availableWeekdaysRepository;
    public AvailableWeekdaysServiceImpl(AvailableWeekdaysRepository availableWeekdaysRepository) {
        this.availableWeekdaysRepository = availableWeekdaysRepository;
    }

    /**
     * Save a availableWeekdays.
     *
     * @param availableWeekdays the entity to save
     * @return the persisted entity
     */
    @Override
    public AvailableWeekdays save(AvailableWeekdays availableWeekdays) {
        log.debug("Request to save AvailableWeekdays : {}", availableWeekdays);
        return availableWeekdaysRepository.save(availableWeekdays);
    }

    /**
     *  Get all the availableWeekdays.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AvailableWeekdays> findAll() {
        log.debug("Request to get all AvailableWeekdays");
        return availableWeekdaysRepository.findAll();
    }

    /**
     *  Get one availableWeekdays by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AvailableWeekdays findOne(Long id) {
        log.debug("Request to get AvailableWeekdays : {}", id);
        return availableWeekdaysRepository.findOne(id);
    }

    /**
     *  Delete the  availableWeekdays by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AvailableWeekdays : {}", id);
        availableWeekdaysRepository.delete(id);
    }
    
    @Override
    public AvailableWeekdays createInstance (DayOfWeek day, DoctorSchedule doctorSchedule) {
    	AvailableWeekdays availableDay = new AvailableWeekdays();
    	availableDay.setWeekday(day);
    	availableDay.setDoctorSchedule(doctorSchedule);
    	return availableDay;
    }
    
    @Override
	public boolean isDeletable(Long id) {
		AvailableWeekdays day = this.findOne(id);
		return SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
				(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.DOCTOR) &&
				SecurityUtils.getCurrentUserLogin().equals(day.getDoctorSchedule().getDoctor().getUser().getLogin()));
	}
}
