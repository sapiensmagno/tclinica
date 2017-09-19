package br.com.tclinica.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * A ExamStatus.
 */
@Entity
@Table(name = "exam_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExamStatus implements Serializable, Comparable<ExamStatus> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @ManyToOne
    @JsonBackReference
    private Exam exam;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ExamStatus name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ExamStatus creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    @JsonBackReference
    public Exam getExam() {
        return exam;
    }
    
    @JsonBackReference
    public ExamStatus exam(Exam exam) {
        this.exam = exam;
        return this;
    }
        
    public void setExam(Exam exam) {
        this.exam = exam;
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
        ExamStatus examStatus = (ExamStatus) o;
        if (examStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), examStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExamStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }

	@Override
	public int compareTo(ExamStatus otherExamStatus) {
		if (otherExamStatus.equals(this)) {
			return 0;
		}
		else {
			return this.getCreationDate().compareTo(otherExamStatus.getCreationDate());
		}
	}
}
