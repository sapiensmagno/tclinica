package br.com.tclinica.service.util;

import org.springframework.data.jpa.repository.JpaRepository;

public class ExistenceUtil {
	
	public static boolean entityDoesntExist(Long id, JpaRepository<?, Long> entityRepository) {
    	return id == null || !entityRepository.exists(id);
	}
	
}