package br.com.tclinica.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PaymentInstallment.
 */
@Entity
@Table(name = "payment_installment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentInstallment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_date")
    private ZonedDateTime payDate;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "jhi_value", precision=10, scale=2)
    private BigDecimal value;

    @Column(name = "jhi_number")
    private Integer number;

    @Column(name = "check_num")
    private String checkNum;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_final")
    private String cardFinal;

    @Column(name = "pay_method")
    private String payMethod;

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

    public ZonedDateTime getPayDate() {
        return payDate;
    }

    public PaymentInstallment payDate(ZonedDateTime payDate) {
        this.payDate = payDate;
        return this;
    }

    public void setPayDate(ZonedDateTime payDate) {
        this.payDate = payDate;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public PaymentInstallment dueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(ZonedDateTime dueDate) {
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

    public Integer getNumber() {
        return number;
    }

    public PaymentInstallment number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public PaymentInstallment checkNum(String checkNum) {
        this.checkNum = checkNum;
        return this;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getCardType() {
        return cardType;
    }

    public PaymentInstallment cardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardFinal() {
        return cardFinal;
    }

    public PaymentInstallment cardFinal(String cardFinal) {
        this.cardFinal = cardFinal;
        return this;
    }

    public void setCardFinal(String cardFinal) {
        this.cardFinal = cardFinal;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public PaymentInstallment payMethod(String payMethod) {
        this.payMethod = payMethod;
        return this;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
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
            ", number='" + getNumber() + "'" +
            ", checkNum='" + getCheckNum() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", cardFinal='" + getCardFinal() + "'" +
            ", payMethod='" + getPayMethod() + "'" +
            "}";
    }
}
