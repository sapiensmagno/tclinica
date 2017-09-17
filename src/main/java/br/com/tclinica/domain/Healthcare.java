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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Healthcare.
 */
@Entity
@Table(name = "healthcare")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Healthcare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;
    
    @NotNull
    @Column(name = "inactive")
    private boolean inactive;

    @OneToMany(mappedBy = "healthcare")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentInstallment> paymentInstallments = new HashSet<>();

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

    public Healthcare name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInactive() {
        return inactive;
    }

    public Healthcare inactive(boolean inactive) {
        this.inactive = inactive;
        return this;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public Set<PaymentInstallment> getPaymentInstallments() {
        return paymentInstallments;
    }

    public Healthcare paymentInstallments(Set<PaymentInstallment> paymentInstallments) {
        this.paymentInstallments = paymentInstallments;
        return this;
    }

    public Healthcare addPaymentInstallment(PaymentInstallment paymentInstallment) {
        this.paymentInstallments.add(paymentInstallment);
        paymentInstallment.setHealthcare(this);
        return this;
    }

    public Healthcare removePaymentInstallment(PaymentInstallment paymentInstallment) {
        this.paymentInstallments.remove(paymentInstallment);
        paymentInstallment.setHealthcare(null);
        return this;
    }

    public void setPaymentInstallments(Set<PaymentInstallment> paymentInstallments) {
        this.paymentInstallments = paymentInstallments;
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
        Healthcare healthcare = (Healthcare) o;
        if (healthcare.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), healthcare.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Healthcare{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", inactive='" + isInactive() + "'" +
            "}";
    }
}
