package br.com.tclinica.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Medicine.
 */
@Entity
@Table(name = "medicine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "generic_name", nullable = false)
    private String genericName;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "inactive")
    private Boolean inactive;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenericName() {
        return genericName;
    }

    public Medicine genericName(String genericName) {
        this.genericName = genericName;
        return this;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public Medicine brandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Medicine manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Boolean isInactive() {
        return inactive;
    }

    public Medicine inactive(Boolean inactive) {
        this.inactive = inactive;
        return this;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
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
        Medicine medicine = (Medicine) o;
        if (medicine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medicine{" +
            "id=" + getId() +
            ", genericName='" + getGenericName() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", inactive='" + isInactive() + "'" +
            "}";
    }
}
