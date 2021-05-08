package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Branch.
 */
@Entity
@Table(name = "branch")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "semester")
    private Set<Semester> semesters = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "branches", allowSetters = true)
    private Domain branches;

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

    public Branch name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Branch description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public Branch semesters(Set<Semester> semesters) {
        this.semesters = semesters;
        return this;
    }

    public Branch addSemester(Semester semester) {
        this.semesters.add(semester);
        semester.setSemester(this);
        return this;
    }

    public Branch removeSemester(Semester semester) {
        this.semesters.remove(semester);
        semester.setSemester(null);
        return this;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

    public Domain getBranches() {
        return branches;
    }

    public Branch branches(Domain domain) {
        this.branches = domain;
        return this;
    }

    public void setBranches(Domain domain) {
        this.branches = domain;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }
        return id != null && id.equals(((Branch) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Branch{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
