package com.apt.wii.repository;

import com.apt.wii.domain.TagMetaData;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TagMetaData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagMetaDataRepository extends JpaRepository<TagMetaData, Long> {
}
