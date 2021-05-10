package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * requirements\nER diagram\nclass diagram
 */
@ApiModel(description = "requirements\nER diagram\nclass diagram")
@Entity
@Table(name = "domain")
public class Domain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "domain")
    @JsonIgnoreProperties(value = { "semesters", "domain" }, allowSetters = true)
    private Set<Branch> branches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Domain id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Domain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Domain description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Branch> getBranches() {
        return this.branches;
    }

    public Domain branches(Set<Branch> branches) {
        this.setBranches(branches);
        return this;
    }

    public Domain addBranches(Branch branch) {
        this.branches.add(branch);
        branch.setDomain(this);
        return this;
    }

    public Domain removeBranches(Branch branch) {
        this.branches.remove(branch);
        branch.setDomain(null);
        return this;
    }

    public void setBranches(Set<Branch> branches) {
        if (this.branches != null) {
            this.branches.forEach(i -> i.setDomain(null));
        }
        if (branches != null) {
            branches.forEach(i -> i.setDomain(this));
        }
        this.branches = branches;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Domain)) {
            return false;
        }
        return id != null && id.equals(((Domain) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Domain{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
