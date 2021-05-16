package com.apt.wii.repository;

import com.apt.wii.domain.Domain;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Domain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomainRepository extends PagingAndSortingRepository<Domain, Long> {}
