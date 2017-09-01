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
 * A Healthcare.
 */
@Entity
@Table(name = "healthcare")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Healthcare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

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
            "}";
    }
}
