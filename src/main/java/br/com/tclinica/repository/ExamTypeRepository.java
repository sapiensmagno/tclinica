package br.com.tclinica.repository;

import br.com.tclinica.domain.ExamType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExamType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Long> {

}
