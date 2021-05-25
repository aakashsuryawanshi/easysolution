package com.apt.wii.repository;

import com.apt.wii.domain.FileDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileDetailsRepository extends JpaRepository<FileDetails, Long> {}
