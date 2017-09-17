package br.com.tclinica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MedicalRecord.
 */
@Entity
@Table(name = "medical_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private Appointment appointment;

    @OneToMany(mappedBy = "medicalRecord")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Exam> exams = new HashSet<>();

    @OneToMany(mappedBy = "medicalRecord")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prescription> prescriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public MedicalRecord description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public MedicalRecord appointment(Appointment appointment) {
        this.appointment = appointment;
        return this;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public MedicalRecord exams(Set<Exam> exams) {
        this.exams = exams;
        return this;
    }

    public MedicalRecord addExam(Exam exam) {
        this.exams.add(exam);
        exam.setMedicalRecord(this);
        return this;
    }

    public MedicalRecord removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.setMedicalRecord(null);
        return this;
    }

    public void setExams(Set<Exam> exams) {
        this.exams = exams;
    }

    public Set<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public MedicalRecord prescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        return this;
    }

    public MedicalRecord addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setMedicalRecord(this);
        return this;
    }

    public MedicalRecord removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setMedicalRecord(null);
        return this;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
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
        MedicalRecord medicalRecord = (MedicalRecord) o;
        if (medicalRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicalRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
