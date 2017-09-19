package br.com.tclinica.repository;

import br.com.tclinica.domain.Receptionist;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Receptionist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
	
	@Override
	default void delete(Long id) {
		this.saveAndFlush(this.findOne(id).inactive(true));
	}
}
