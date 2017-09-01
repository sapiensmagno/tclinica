package br.com.tclinica.repository;

import br.com.tclinica.domain.AvailableWeekdays;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AvailableWeekdays entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableWeekdaysRepository extends JpaRepository<AvailableWeekdays, Long> {

}
