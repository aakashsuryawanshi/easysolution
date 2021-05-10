package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

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

    @OneToMany(mappedBy = "branch")
    @JsonIgnoreProperties(value = { "subjects", "branch" }, allowSetters = true)
    private Set<Semester> semesters = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "branches" }, allowSetters = true)
    private Domain domain;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Branch id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Branch name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Branch description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Semester> getSemesters() {
        return this.semesters;
    }

    public Branch semesters(Set<Semester> semesters) {
        this.setSemesters(semesters);
        return this;
    }

    public Branch addSemester(Semester semester) {
        this.semesters.add(semester);
        semester.setBranch(this);
        return this;
    }

    public Branch removeSemester(Semester semester) {
        this.semesters.remove(semester);
        semester.setBranch(null);
        return this;
    }

    public void setSemesters(Set<Semester> semesters) {
        if (this.semesters != null) {
            this.semesters.forEach(i -> i.setBranch(null));
        }
        if (semesters != null) {
            semesters.forEach(i -> i.setBranch(this));
        }
        this.semesters = semesters;
    }

    public Domain getDomain() {
        return this.domain;
    }

    public Branch domain(Domain domain) {
        this.setDomain(domain);
        return this;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
