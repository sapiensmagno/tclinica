package br.com.tclinica.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentInstallment.
 */
@Entity
@Table(name = "payment_installment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentInstallment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "jhi_value", precision=10, scale=2)
    private BigDecimal value;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "check_number")
    private String checkNumber;

    @Column(name = "card_final_number")
    private String cardFinalNumber;
    
    @NotNull
    @Column(name = "cancelled")
    private boolean cancelled;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = false)
    private PaymentMethod paymentMethod;

    @OneToOne
    @JoinColumn(unique = true)
    private CardBrand cardBrand;

    @ManyToOne
    private Healthcare healthcare;

    @ManyToOne
    private Appointment appointment;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public PaymentInstallment payDate(LocalDate payDate) {
        this.payDate = payDate;
        return this;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public PaymentInstallment dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public PaymentInstallment value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public PaymentInstallment installmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
        return this;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public PaymentInstallment checkNumber(String checkNumber) {
        this.checkNumber = checkNumber;
        return this;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCardFinalNumber() {
        return cardFinalNumber;
    }

    public PaymentInstallment cardFinalNumber(String cardFinalNumber) {
        this.cardFinalNumber = cardFinalNumber;
        return this;
    }

    public void setCardFinalNumber(String cardFinalNumber) {
        this.cardFinalNumber = cardFinalNumber;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public PaymentInstallment cancelled(boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentInstallment paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CardBrand getCardBrand() {
        return cardBrand;
    }

    public PaymentInstallment cardBrand(CardBrand cardBrand) {
        this.cardBrand = cardBrand;
        return this;
    }

    public void setCardBrand(CardBrand cardBrand) {
        this.cardBrand = cardBrand;
    }

    public Healthcare getHealthcare() {
        return healthcare;
    }

    public PaymentInstallment healthcare(Healthcare healthcare) {
        this.healthcare = healthcare;
        return this;
    }

    public void setHealthcare(Healthcare healthcare) {
        this.healthcare = healthcare;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public PaymentInstallment appointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentInstallment paymentInstallment = (PaymentInstallment) o;
        if (paymentInstallment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentInstallment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentInstallment{" +
            "id=" + getId() +
            ", payDate='" + getPayDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", value='" + getValue() + "'" +
            ", installmentNumber='" + getInstallmentNumber() + "'" +
            ", checkNumber='" + getCheckNumber() + "'" +
            ", cardFinalNumber='" + getCardFinalNumber() + "'" +
            ", cancelled='" + isCancelled() + "'" +
            "}";
    }
}
