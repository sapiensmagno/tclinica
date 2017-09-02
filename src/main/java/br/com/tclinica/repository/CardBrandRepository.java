package br.com.tclinica.repository;

import br.com.tclinica.domain.CardBrand;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CardBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardBrandRepository extends JpaRepository<CardBrand, Long> {

}
