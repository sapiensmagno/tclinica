package br.com.tclinica.domain;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AvailableWeekdays.
 */
@Entity
@Table(name = "available_weekdays")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailableWeekdays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "weekday", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DayOfWeek weekday;

    @ManyToOne
    private DoctorSchedule doctorSchedule;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getWeekday() {
        return weekday;
    }

    public AvailableWeekdays weekday(DayOfWeek weekday) {
        this.weekday = weekday;
        return this;
    }

    public void setWeekday(DayOfWeek weekday) {
        this.weekday = weekday;
    }

    public DoctorSchedule getDoctorSchedule() {
        return doctorSchedule;
    }

    public AvailableWeekdays doctorSchedule(DoctorSchedule doctorSchedule) {
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
        AvailableWeekdays availableWeekdays = (AvailableWeekdays) o;
        if (availableWeekdays.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), availableWeekdays.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AvailableWeekdays{" +
            "id=" + getId() +
            ", weekday='" + getWeekday() + "'" +
            "}";
    }
}
