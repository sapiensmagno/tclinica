package br.com.tclinica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Appointment.
 */
@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "scheduled_date", nullable = false)
    private ZonedDateTime scheduledDate;

    @Column(name = "cancelled")
    private Boolean cancelled;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentInstallment> paymentInstallments = new HashSet<>();

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getScheduledDate() {
        return scheduledDate;
    }

    public Appointment scheduledDate(ZonedDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    public void setScheduledDate(ZonedDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Boolean isCancelled() {
        return cancelled;
    }

    public Appointment cancelled(Boolean cancelled) {
        this.cancelled = cancelled;
        return this;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Set<PaymentInstallment> getPaymentInstallments() {
        return paymentInstallments;
    }

    public Appointment paymentInstallments(Set<PaymentInstallment> paymentInstallments) {
        this.paymentInstallments = paymentInstallments;
        return this;
    }

    public Appointment addPaymentInstallment(PaymentInstallment paymentInstallment) {
        this.paymentInstallments.add(paymentInstallment);
        paymentInstallment.setAppointment(this);
        return this;
    }

    public Appointment removePaymentInstallment(PaymentInstallment paymentInstallment) {
        this.paymentInstallments.remove(paymentInstallment);
        paymentInstallment.setAppointment(null);
        return this;
    }

    public void setPaymentInstallments(Set<PaymentInstallment> paymentInstallments) {
        this.paymentInstallments = paymentInstallments;
    }

    public Patient getPatient() {
        return patient;
    }

    public Appointment patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Appointment doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
        Appointment appointment = (Appointment) o;
        if (appointment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + getId() +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", cancelled='" + isCancelled() + "'" +
            "}";
    }
}
