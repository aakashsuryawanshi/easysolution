package com.apt.wii.repository;

import com.apt.wii.domain.Branch;
import com.apt.wii.domain.Domain;
import com.apt.wii.domain.Question;
import com.apt.wii.domain.Subject;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Branch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BranchRepository extends PagingAndSortingRepository<Branch, Long> {
    List<Branch> findByDomain(Domain domain, Pageable paging);
}
