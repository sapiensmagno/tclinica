package br.com.tclinica.repository;

import br.com.tclinica.domain.Accountant;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Accountant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountantRepository extends JpaRepository<Accountant, Long> {

}
