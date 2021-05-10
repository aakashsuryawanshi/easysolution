package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Semester.
 */
@Entity
@Table(name = "semester")
public class Semester implements Serializable {

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
    @JsonIgnoreProperties(value = { "questions", "semester" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "semesters", "domain" }, allowSetters = true)
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Semester id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Semester name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Semester description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public Semester subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Semester addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setSemester(this);
        return this;
    }

    public Semester removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.setSemester(null);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        if (this.subjects != null) {
            this.subjects.forEach(i -> i.setSemester(null));
        }
        if (subjects != null) {
            subjects.forEach(i -> i.setSemester(this));
        }
        this.subjects = subjects;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public Semester branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semester)) {
            return false;
        }
        return id != null && id.equals(((Semester) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semester{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
