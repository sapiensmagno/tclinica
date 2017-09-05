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
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "cancelled")
    private Boolean cancelled;

    @OneToMany(mappedBy = "appointment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentInstallment> paymentInstallments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @ManyToOne(optional = false)
    @NotNull
    private DoctorSchedule doctorSchedule;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public Appointment startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Appointment endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public Appointment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isCancelled() {
    	if (cancelled == null) {
    		return false;
    	}
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

    public DoctorSchedule getDoctorSchedule() {
        return doctorSchedule;
    }

    public Appointment doctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedule = doctorSchedule;
        return this;
    }

    public void setDoctorSchedule(DoctorSchedule doctorSchedule) {
        this.doctorSchedule = doctorSchedule;
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
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", cancelled='" + isCancelled() + "'" +
            "}";
    }
}