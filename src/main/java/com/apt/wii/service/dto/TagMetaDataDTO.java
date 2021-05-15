package com.apt.wii.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apt.wii.domain.TagMetaData} entity.
 */
@ApiModel(description = "The Employee entity.")
public class TagMetaDataDTO implements Serializable {

    private Long id;

    private String key;

    private String value;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(o instanceof TagMetaDataDTO)) {
            return false;
        }

        TagMetaDataDTO tagMetaDataDTO = (TagMetaDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagMetaDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagMetaDataDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", question=" + getQuestion() +
            "}";
    }
}
