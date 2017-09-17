package br.com.tclinica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tclinica.domain.Accountant;


/**
 * Spring Data JPA repository for the Accountant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountantRepository extends JpaRepository<Accountant, Long> {

	@Override
	default void delete(Long id) {
		this.saveAndFlush(this.findOne(id).inactive(true));
	}
}
