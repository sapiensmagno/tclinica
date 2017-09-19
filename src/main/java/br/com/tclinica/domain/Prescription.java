package br.com.tclinica.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prescription.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "jhi_text", nullable = true)
    private String text;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "prescription_medicine",
               joinColumns = @JoinColumn(name="prescriptions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="medicines_id", referencedColumnName="id"))
    private Set<Medicine> medicines = new HashSet<>();

    @ManyToOne
    @NotNull
    private MedicalRecord medicalRecord;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Prescription text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
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
            ", number='" + getText() + "'" +
            "}";
    }
}
