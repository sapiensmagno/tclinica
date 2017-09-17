package br.com.tclinica.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private ExamType examType;

    @OneToMany(mappedBy = "exam")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ExamStatus> examStatuses = new HashSet<>();

    @ManyToOne
    private MedicalRecord medicalRecord;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExamType getExamType() {
        return examType;
    }

    public Exam examType(ExamType examType) {
        this.examType = examType;
        return this;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public Set<ExamStatus> getExamStatuses() {
        return examStatuses;
    }

    public Exam examStatuses(Set<ExamStatus> examStatuses) {
        this.examStatuses = examStatuses;
        return this;
    }

    public Exam addExamStatus(ExamStatus examStatus) {
        this.examStatuses.add(examStatus);
        examStatus.setExam(this);
        return this;
    }

    public Exam removeExamStatus(ExamStatus examStatus) {
        this.examStatuses.remove(examStatus);
        examStatus.setExam(null);
        return this;
    }

    public void setExamStatuses(Set<ExamStatus> examStatuses) {
        this.examStatuses = examStatuses;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public Exam medicalRecord(MedicalRecord medicalRecord) {
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
        Exam exam = (Exam) o;
        if (exam.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exam.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            "}";
    }
}
