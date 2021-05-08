package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "question")
    private Set<Question> questions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "subjects", allowSetters = true)
    private Semester subject;

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

    public Subject name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Subject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Subject questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Subject addQuestion(Question question) {
        this.questions.add(question);
        question.setQuestion(this);
        return this;
    }

    public Subject removeQuestion(Question question) {
        this.questions.remove(question);
        question.setQuestion(null);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Semester getSubject() {
        return subject;
    }

    public Subject subject(Semester semester) {
        this.subject = semester;
        return this;
    }

    public void setSubject(Semester semester) {
        this.subject = semester;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
