package com.apt.wii.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apt.wii.domain.FileMetaData} entity.
 */
public class FileMetaDataDTO implements Serializable {

    private Long id;

    private String key;

    private String value;

    private FileDetailsDTO fileDetails;

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

    public FileDetailsDTO getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(FileDetailsDTO fileDetails) {
        this.fileDetails = fileDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileMetaDataDTO)) {
            return false;
        }

        FileMetaDataDTO fileMetaDataDTO = (FileMetaDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileMetaDataDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileMetaDataDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", fileDetails=" + getFileDetails() +
            "}";
    }
}
