package br.com.tclinica.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Prescription.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_number", nullable = false)
    private Integer number;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "prescription_medicine",
               joinColumns = @JoinColumn(name="prescriptions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="medicines_id", referencedColumnName="id"))
    private Set<Medicine> medicines = new HashSet<>();

    @ManyToOne
    private MedicalRecord medicalRecord;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Prescription number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Set<Medicine> getMedicines() {
        return medicines;
    }

    public Prescription medicines(Set<Medicine> medicines) {
        this.medicines = medicines;
        return this;
    }

    public Prescription addMedicine(Medicine medicine) {
        this.medicines.add(medicine);
        return this;
    }

    public Prescription removeMedicine(Medicine medicine) {
        this.medicines.remove(medicine);
        return this;
    }

    public void setMedicines(Set<Medicine> medicines) {
        this.medicines = medicines;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public Prescription medicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
        return this;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
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
        Prescription prescription = (Prescription) o;
        if (prescription.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prescription.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prescription{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            "}";
    }
}
