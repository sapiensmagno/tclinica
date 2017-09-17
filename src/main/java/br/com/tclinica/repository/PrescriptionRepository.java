package br.com.tclinica.repository;

import br.com.tclinica.domain.Prescription;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query("select distinct prescription from Prescription prescription left join fetch prescription.medicines")
    List<Prescription> findAllWithEagerRelationships();

    @Query("select prescription from Prescription prescription left join fetch prescription.medicines where prescription.id =:id")
    Prescription findOneWithEagerRelationships(@Param("id") Long id);

}
