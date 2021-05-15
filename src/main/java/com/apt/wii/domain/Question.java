package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "topic")
    private String topic;

    @OneToMany(mappedBy = "question")
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<TagMetaData> tags = new HashSet<>();

    @OneToMany(mappedBy = "question")
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<Content> answers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "questions", "semester" }, allowSetters = true)
    private Subject subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Question title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Question description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return this.topic;
    }

    public Question topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Set<TagMetaData> getTags() {
        return this.tags;
    }

    public Question tags(Set<TagMetaData> tagMetaData) {
        this.setTags(tagMetaData);
        return this;
    }

    public Question addTags(TagMetaData tagMetaData) {
        this.tags.add(tagMetaData);
        tagMetaData.setQuestion(this);
        return this;
    }

    public Question removeTags(TagMetaData tagMetaData) {
        this.tags.remove(tagMetaData);
        tagMetaData.setQuestion(null);
        return this;
    }

    public void setTags(Set<TagMetaData> tagMetaData) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setQuestion(null));
        }
        if (tagMetaData != null) {
            tagMetaData.forEach(i -> i.setQuestion(this));
        }
        this.tags = tagMetaData;
    }

    public Set<Content> getAnswers() {
        return this.answers;
    }

    public Question answers(Set<Content> contents) {
        this.setAnswers(contents);
        return this;
    }

    public Question addAnswers(Content content) {
        this.answers.add(content);
        content.setQuestion(this);
        return this;
    }

    public Question removeAnswers(Content content) {
        this.answers.remove(content);
        content.setQuestion(null);
        return this;
    }

    public void setAnswers(Set<Content> contents) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setQuestion(null));
        }
        if (contents != null) {
            contents.forEach(i -> i.setQuestion(this));
        }
        this.answers = contents;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public Question subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", topic='" + getTopic() + "'" +
            "}";
    }
}
