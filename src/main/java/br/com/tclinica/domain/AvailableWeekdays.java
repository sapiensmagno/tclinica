package br.com.tclinica.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.com.tclinica.domain.enumeration.Weekdays;

/**
 * A AvailableWeekdays.
 */
@Entity
@Table(name = "available_weekdays")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AvailableWeekdays implements Serializable {

    private static final long serialVersionUID = 1L;
        
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weekday", nullable = false)
    private Weekdays weekday;

    @ManyToOne
    private DoctorSchedule doctorSchedule;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weekdays getWeekday() {
        return weekday;
    }

    public AvailableWeekdays weekday(Weekdays weekday) {
        this.weekday = weekday;
        return this;
    }

    public void setWeekday(Weekdays weekday) {
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
    
public static Set<AvailableWeekdays> getWorkingDays() {
	Set<AvailableWeekdays> workingDays = new HashSet<>();
	AvailableWeekdays monday = new AvailableWeekdays();
	AvailableWeekdays tuesday = new AvailableWeekdays();
	AvailableWeekdays wednesday = new AvailableWeekdays();
	AvailableWeekdays thursday = new AvailableWeekdays();
	AvailableWeekdays friday = new AvailableWeekdays();
	monday.setWeekday(Weekdays.MONDAY);
	tuesday.setWeekday(Weekdays.TUESDAY);
	wednesday.setWeekday(Weekdays.WEDNESDAY);
	thursday.setWeekday(Weekdays.THURSDAY);
	friday.setWeekday(Weekdays.FRIDAY);
	workingDays.add(monday);
	workingDays.add(tuesday);
	workingDays.add(wednesday);
	workingDays.add(thursday);
	workingDays.add(friday);
	return workingDays;
		
	}
}
