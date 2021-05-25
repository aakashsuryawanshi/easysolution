package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A FileDetails.
 */
@Entity
@Table(name = "file_details")
public class FileDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "destination_name")
    private String destinationName;

    @Column(name = "destination")
    private String destination;

    @Column(name = "meta_data")
    private String metaData;

    @OneToMany(mappedBy = "fileDetails")
    @JsonIgnoreProperties(value = { "fileDetails" }, allowSetters = true)
    private Set<FileMetaData> metadata = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public FileDetails sourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getDestinationName() {
        return this.destinationName;
    }

    public FileDetails destinationName(String destinationName) {
        this.destinationName = destinationName;
        return this;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestination() {
        return this.destination;
    }

    public FileDetails destination(String destination) {
        this.destination = destination;
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public FileDetails metaData(String metaData) {
        this.metaData = metaData;
        return this;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Set<FileMetaData> getMetadata() {
        return this.metadata;
    }

    public FileDetails metadata(Set<FileMetaData> fileMetaData) {
        this.setMetadata(fileMetaData);
        return this;
    }

    public FileDetails addMetadata(FileMetaData fileMetaData) {
        this.metadata.add(fileMetaData);
        fileMetaData.setFileDetails(this);
        return this;
    }

    public FileDetails removeMetadata(FileMetaData fileMetaData) {
        this.metadata.remove(fileMetaData);
        fileMetaData.setFileDetails(null);
        return this;
    }

    public void setMetadata(Set<FileMetaData> fileMetaData) {
        if (this.metadata != null) {
            this.metadata.forEach(i -> i.setFileDetails(null));
        }
        if (fileMetaData != null) {
            fileMetaData.forEach(i -> i.setFileDetails(this));
        }
        this.metadata = fileMetaData;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDetails)) {
            return false;
        }
        return id != null && id.equals(((FileDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileDetails{" +
            "id=" + getId() +
            ", sourceName='" + getSourceName() + "'" +
            ", destinationName='" + getDestinationName() + "'" +
            ", destination='" + getDestination() + "'" +
            ", metaData='" + getMetaData() + "'" +
            "}";
    }
}
