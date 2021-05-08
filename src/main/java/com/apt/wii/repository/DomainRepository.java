package com.apt.wii.repository;

import com.apt.wii.domain.Domain;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Domain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
}
