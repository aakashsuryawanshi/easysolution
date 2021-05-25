package com.apt.wii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A FileMetaData.
 */
@Entity
@Table(name = "file_meta_data")
public class FileMetaData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "metadata" }, allowSetters = true)
    private FileDetails fileDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileMetaData id(Long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public FileMetaData key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public FileMetaData value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FileDetails getFileDetails() {
        return this.fileDetails;
    }

    public FileMetaData fileDetails(FileDetails fileDetails) {
        this.setFileDetails(fileDetails);
        return this;
    }

    public void setFileDetails(FileDetails fileDetails) {
        this.fileDetails = fileDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileMetaData)) {
            return false;
        }
        return id != null && id.equals(((FileMetaData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileMetaData{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
