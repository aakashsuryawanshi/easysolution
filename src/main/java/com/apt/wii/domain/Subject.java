package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

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

    @OneToMany(mappedBy = "subject")
    @JsonIgnoreProperties(value = { "tags", "answers", "subject" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subjects", "branch" }, allowSetters = true)
    private Semester semester;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Subject name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Subject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public Subject questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Subject addQuestion(Question question) {
        this.questions.add(question);
        question.setSubject(this);
        return this;
    }

    public Subject removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSubject(null);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setSubject(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setSubject(this));
        }
        this.questions = questions;
    }

    public Semester getSemester() {
        return this.semester;
    }

    public Subject semester(Semester semester) {
        this.setSemester(semester);
        return this;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
