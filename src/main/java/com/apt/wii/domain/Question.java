package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Task entity.\n@author The JHipster team.
 */
@ApiModel(description = "Task entity.\n@author The JHipster team.")
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

    @OneToMany(mappedBy = "tags")
    private Set<TagMetaData> tagMetaData = new HashSet<>();

    @OneToMany(mappedBy = "answers")
    private Set<Content> contents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "questions", allowSetters = true)
    private Subject question;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Question title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Question description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public Question topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Set<TagMetaData> getTagMetaData() {
        return tagMetaData;
    }

    public Question tagMetaData(Set<TagMetaData> tagMetaData) {
        this.tagMetaData = tagMetaData;
        return this;
    }

    public Question addTagMetaData(TagMetaData tagMetaData) {
        this.tagMetaData.add(tagMetaData);
        tagMetaData.setTags(this);
        return this;
    }

    public Question removeTagMetaData(TagMetaData tagMetaData) {
        this.tagMetaData.remove(tagMetaData);
        tagMetaData.setTags(null);
        return this;
    }

    public void setTagMetaData(Set<TagMetaData> tagMetaData) {
        this.tagMetaData = tagMetaData;
    }

    public Set<Content> getContents() {
        return contents;
    }

    public Question contents(Set<Content> contents) {
        this.contents = contents;
        return this;
    }

    public Question addContent(Content content) {
        this.contents.add(content);
        content.setAnswers(this);
        return this;
    }

    public Question removeContent(Content content) {
        this.contents.remove(content);
        content.setAnswers(null);
        return this;
    }

    public void setContents(Set<Content> contents) {
        this.contents = contents;
    }

    public Subject getQuestion() {
        return question;
    }

    public Question question(Subject subject) {
        this.question = subject;
        return this;
    }

    public void setQuestion(Subject subject) {
        this.question = subject;
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
        return 31;
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
