package br.com.tclinica.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tclinica.domain.Appointment;
import br.com.tclinica.domain.DoctorSchedule;
import br.com.tclinica.domain.Patient;


/**
 * Spring Data JPA repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	public List<Appointment> findByDoctorSchedule (DoctorSchedule doctorSchedule);
	
	public Page<Appointment> findByDoctorSchedule (DoctorSchedule doctorSchedule, Pageable pageable);
	
	@Override
	default void delete(Long id) {
		this.saveAndFlush(this.findOne(id).cancelled(true));
	}

	public Page<Appointment> findByPatient(Patient patient, Pageable pageable);
}