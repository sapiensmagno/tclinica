package br.com.tclinica.service.impl;

import br.com.tclinica.service.DoctorService;
import br.com.tclinica.domain.Doctor;
import br.com.tclinica.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Doctor.
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService{

    private final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;
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
        return doctorRepository.save(doctor);
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
