package br.com.tclinica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.DoctorSchedule;


/**
 * Spring Data JPA repository for the DoctorSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
	
	public DoctorSchedule findByDoctor(Doctor doctor);

}
