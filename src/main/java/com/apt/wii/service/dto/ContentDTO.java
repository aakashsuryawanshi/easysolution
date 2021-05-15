package com.apt.wii.service.dto;

import com.apt.wii.domain.enumeration.ContentType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apt.wii.domain.Content} entity.
 */
public class ContentDTO implements Serializable {

    private Long id;

    private ContentType type;

    private String text;

    private String filePath;

    private Integer seqNum;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentDTO)) {
            return false;
        }

        ContentDTO contentDTO = (ContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", seqNum=" + getSeqNum() +
            ", question=" + getQuestion() +
            "}";
    }
}
