package br.com.tclinica.repository;

import br.com.tclinica.domain.ExamStatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExamStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamStatusRepository extends JpaRepository<ExamStatus, Long> {

}
