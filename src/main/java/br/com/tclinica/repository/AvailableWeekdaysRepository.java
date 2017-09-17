package br.com.tclinica.repository;

import br.com.tclinica.domain.AvailableWeekdays;
import br.com.tclinica.domain.DoctorSchedule;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AvailableWeekdays entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableWeekdaysRepository extends JpaRepository<AvailableWeekdays, Long> {

	List<AvailableWeekdays> findByDoctorSchedule(DoctorSchedule doctorSchedule);

}
