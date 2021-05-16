package com.apt.wii.domain;

import com.apt.wii.domain.enumeration.ContentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Content.
 */
@Entity
@Table(name = "content")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ContentType type;

    @Column(name = "text", length=3000)
    private String text;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "seq_num")
    private Integer seqNum;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tags", "answers", "subject" }, allowSetters = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Content id(Long id) {
        this.id = id;
        return this;
    }

    public ContentType getType() {
        return this.type;
    }

    public Content type(ContentType type) {
        this.type = type;
        return this;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public Content text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Content filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSeqNum() {
        return this.seqNum;
    }

    public Content seqNum(Integer seqNum) {
        this.seqNum = seqNum;
        return this;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public Question getQuestion() {
        return this.question;
    }

    public Content question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        return id != null && id.equals(((Content) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Content{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", seqNum=" + getSeqNum() +
            "}";
    }
}
