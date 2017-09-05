package br.com.tclinica.repository;

import br.com.tclinica.domain.MedicalRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MedicalRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

}
