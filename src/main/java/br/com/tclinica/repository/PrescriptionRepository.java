package br.com.tclinica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.tclinica.domain.Prescription;

/**
 * Spring Data JPA repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Transactional(readOnly = true)
	@Query("select distinct prescription from Prescription prescription left join fetch prescription.medicines")
    List<Prescription> findAllWithEagerRelationships();
    
    @Transactional(readOnly = true)
    @Query("select prescription from Prescription prescription left join fetch prescription.medicines where prescription.id =:id")
    Prescription findOneWithEagerRelationships(@Param("id") Long id);

}
