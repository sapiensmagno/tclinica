package br.com.tclinica.repository;

import br.com.tclinica.domain.DoctorSchedule;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DoctorSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

}
