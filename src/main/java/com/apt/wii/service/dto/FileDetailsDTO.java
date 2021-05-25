package com.apt.wii.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apt.wii.domain.FileDetails} entity.
 */
public class FileDetailsDTO implements Serializable {

    private Long id;

    private String sourceName;

    private String destinationName;

    private String destination;

    private String metaData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDetailsDTO)) {
            return false;
        }

        FileDetailsDTO fileDetailsDTO = (FileDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDetailsDTO{" +
            "id=" + getId() +
            ", sourceName='" + getSourceName() + "'" +
            ", destinationName='" + getDestinationName() + "'" +
            ", destination='" + getDestination() + "'" +
            ", metaData='" + getMetaData() + "'" +
            "}";
    }
}
