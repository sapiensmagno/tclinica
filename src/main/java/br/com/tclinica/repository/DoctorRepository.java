package br.com.tclinica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.tclinica.domain.Doctor;
import br.com.tclinica.domain.User;


/**
 * Spring Data JPA repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	@Override
	default void delete(Long id) {
		this.saveAndFlush(this.findOne(id).inactive(true));
	}

	Doctor findByUser(User user);
}
