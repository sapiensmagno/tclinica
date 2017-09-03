package br.com.tclinica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DoctorSchedule.
 */
@Entity
@Table(name = "doctor_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "appointments_duration_minutes", nullable = false)
    private Integer appointmentsDurationMinutes;

    @Column(name = "interval_between_appointments_minutes")
    private Integer intervalBetweenAppointmentsMinutes;

    @NotNull
    @Column(name = "earliest_appointment_time", nullable = false)
    private Instant earliestAppointmentTime;

    @NotNull
    @Column(name = "latest_appointment_time", nullable = false)
    private Instant latestAppointmentTime;

    @Column(name = "calendar_id")
    private String calendarId;

    @NotNull
    @OneToOne
    @JoinColumn(unique = true)
    private Doctor doctor;

    @OneToMany(mappedBy = "doctorSchedule")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AvailableWeekdays> availableWeekdays = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAppointmentsDurationMinutes() {
        return appointmentsDurationMinutes;
    }

    public DoctorSchedule appointmentsDurationMinutes(Integer appointmentsDurationMinutes) {
        this.appointmentsDurationMinutes = appointmentsDurationMinutes;
        return this;
    }

    public void setAppointmentsDurationMinutes(Integer appointmentsDurationMinutes) {
        this.appointmentsDurationMinutes = appointmentsDurationMinutes;
    }

    public Integer getIntervalBetweenAppointmentsMinutes() {
        return intervalBetweenAppointmentsMinutes;
    }

    public DoctorSchedule intervalBetweenAppointmentsMinutes(Integer intervalBetweenAppointmentsMinutes) {
        this.intervalBetweenAppointmentsMinutes = intervalBetweenAppointmentsMinutes;
        return this;
    }

    public void setIntervalBetweenAppointmentsMinutes(Integer intervalBetweenAppointmentsMinutes) {
        this.intervalBetweenAppointmentsMinutes = intervalBetweenAppointmentsMinutes;
    }

    public Instant getEarliestAppointmentTime() {
        return earliestAppointmentTime;
    }

    public DoctorSchedule earliestAppointmentTime(Instant earliestAppointmentTime) {
        this.earliestAppointmentTime = earliestAppointmentTime;
        return this;
    }

    public void setEarliestAppointmentTime(Instant earliestAppointmentTime) {
        this.earliestAppointmentTime = earliestAppointmentTime;
    }

    public Instant getLatestAppointmentTime() {
        return latestAppointmentTime;
    }

    public DoctorSchedule latestAppointmentTime(Instant latestAppointmentTime) {
        this.latestAppointmentTime = latestAppointmentTime;
        return this;
    }

    public void setLatestAppointmentTime(Instant latestAppointmentTime) {
        this.latestAppointmentTime = latestAppointmentTime;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public DoctorSchedule calendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public DoctorSchedule doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Set<AvailableWeekdays> getAvailableWeekdays() {
        return availableWeekdays;
    }

    public DoctorSchedule availableWeekdays(Set<AvailableWeekdays> availableWeekdays) {
        this.availableWeekdays = availableWeekdays;
        return this;
    }

    public DoctorSchedule addAvailableWeekdays(AvailableWeekdays availableWeekdays) {
        this.availableWeekdays.add(availableWeekdays);
        availableWeekdays.setDoctorSchedule(this);
        return this;
    }

    public DoctorSchedule removeAvailableWeekdays(AvailableWeekdays availableWeekdays) {
        this.availableWeekdays.remove(availableWeekdays);
        availableWeekdays.setDoctorSchedule(null);
        return this;
    }

    public void setAvailableWeekdays(Set<AvailableWeekdays> availableWeekdays) {
        this.availableWeekdays = availableWeekdays;
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
        DoctorSchedule doctorSchedule = (DoctorSchedule) o;
        if (doctorSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doctorSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DoctorSchedule{" +
            "id=" + getId() +
            ", appointmentsDurationMinutes='" + getAppointmentsDurationMinutes() + "'" +
            ", intervalBetweenAppointmentsMinutes='" + getIntervalBetweenAppointmentsMinutes() + "'" +
            ", earliestAppointmentTime='" + getEarliestAppointmentTime() + "'" +
            ", latestAppointmentTime='" + getLatestAppointmentTime() + "'" +
            ", calendarId='" + getCalendarId() + "'" +
            "}";
    }
}
