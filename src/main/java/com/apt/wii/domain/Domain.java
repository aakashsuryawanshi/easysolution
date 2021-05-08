package com.apt.wii.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "branches")
    private Set<Branch> branches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Domain name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Domain description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Branch> getBranches() {
        return branches;
    }

    public Domain branches(Set<Branch> branches) {
        this.branches = branches;
        return this;
    }

    public Domain addBranch(Branch branch) {
        this.branches.add(branch);
        branch.setBranches(this);
        return this;
    }

    public Domain removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.setBranches(null);
        return this;
    }

    public void setBranches(Set<Branch> branches) {
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
        return 31;
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
