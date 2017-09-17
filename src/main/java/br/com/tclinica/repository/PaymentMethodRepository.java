package br.com.tclinica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tclinica.domain.PaymentMethod;


/**
 * Spring Data JPA repository for the PaymentMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

	@Override
	default void delete(Long id) {
		this.saveAndFlush(this.findOne(id).inactive(true));
	}
}
