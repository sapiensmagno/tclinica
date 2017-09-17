package br.com.tclinica.service.impl;

import br.com.tclinica.service.PaymentInstallmentService;
import br.com.tclinica.domain.PaymentInstallment;
import br.com.tclinica.repository.PaymentInstallmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing PaymentInstallment.
 */
@Service
@Transactional
public class PaymentInstallmentServiceImpl implements PaymentInstallmentService{

    private final Logger log = LoggerFactory.getLogger(PaymentInstallmentServiceImpl.class);

    private final PaymentInstallmentRepository paymentInstallmentRepository;
    public PaymentInstallmentServiceImpl(PaymentInstallmentRepository paymentInstallmentRepository) {
        this.paymentInstallmentRepository = paymentInstallmentRepository;
    }

    /**
     * Save a paymentInstallment.
     *
     * @param paymentInstallment the entity to save
     * @return the persisted entity
     */
    @Override
    public PaymentInstallment save(PaymentInstallment paymentInstallment) {
        log.debug("Request to save PaymentInstallment : {}", paymentInstallment);
        return paymentInstallmentRepository.save(paymentInstallment);
    }

    /**
     *  Get all the paymentInstallments.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallment> findAll() {
        log.debug("Request to get all PaymentInstallments");
        return paymentInstallmentRepository.findAll();
    }

    /**
     *  Get one paymentInstallment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PaymentInstallment findOne(Long id) {
        log.debug("Request to get PaymentInstallment : {}", id);
        return paymentInstallmentRepository.findOne(id);
    }

    /**
     *  Delete the  paymentInstallment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentInstallment : {}", id);
        paymentInstallmentRepository.delete(id);
    }
}
