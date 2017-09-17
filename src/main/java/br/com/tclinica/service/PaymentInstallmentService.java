package br.com.tclinica.service;

import br.com.tclinica.domain.PaymentInstallment;
import java.util.List;

/**
 * Service Interface for managing PaymentInstallment.
 */
public interface PaymentInstallmentService {

    /**
     * Save a paymentInstallment.
     *
     * @param paymentInstallment the entity to save
     * @return the persisted entity
     */
    PaymentInstallment save(PaymentInstallment paymentInstallment);

    /**
     *  Get all the paymentInstallments.
     *
     *  @return the list of entities
     */
    List<PaymentInstallment> findAll();

    /**
     *  Get the "id" paymentInstallment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PaymentInstallment findOne(Long id);

    /**
     *  Delete the "id" paymentInstallment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
