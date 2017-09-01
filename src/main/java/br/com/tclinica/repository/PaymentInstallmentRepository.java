package br.com.tclinica.repository;

import br.com.tclinica.domain.PaymentInstallment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentInstallment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentInstallmentRepository extends JpaRepository<PaymentInstallment, Long> {

}
