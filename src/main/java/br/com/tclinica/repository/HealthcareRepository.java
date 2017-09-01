package br.com.tclinica.repository;

import br.com.tclinica.domain.Healthcare;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Healthcare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthcareRepository extends JpaRepository<Healthcare, Long> {

}
