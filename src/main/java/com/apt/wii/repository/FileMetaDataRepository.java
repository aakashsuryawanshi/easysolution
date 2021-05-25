package com.apt.wii.repository;

import com.apt.wii.domain.Branch;
import com.apt.wii.domain.Domain;
import com.apt.wii.domain.FileDetails;
import com.apt.wii.domain.FileMetaData;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileMetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {
    List<FileMetaData> findByFileDetails(FileDetails fileDetails);
}
