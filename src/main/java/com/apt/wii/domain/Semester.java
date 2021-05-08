package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "subject")
    private Set<Subject> subjects = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "semesters", allowSetters = true)
    private Branch semester;

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

    public Semester name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Semester description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public Semester subjects(Set<Subject> subjects) {
        this.subjects = subjects;
        return this;
    }

    public Semester addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setSubject(this);
        return this;
    }

    public Semester removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.setSubject(null);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Branch getSemester() {
        return semester;
    }

    public Semester semester(Branch branch) {
        this.semester = branch;
        return this;
    }

    public void setSemester(Branch branch) {
        this.semester = branch;
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
        return 31;
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
