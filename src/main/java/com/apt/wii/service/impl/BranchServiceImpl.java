package com.apt.wii.service.impl;

import com.apt.wii.domain.Branch;
import com.apt.wii.repository.BranchRepository;
import com.apt.wii.service.BranchService;
import com.apt.wii.service.DomainService;
import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.DomainDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.mapper.BranchMapper;
import com.apt.wii.service.mapper.DomainMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Branch}.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final DomainMapper domainMapper;

    private final DomainService domainService;

    public BranchServiceImpl(
        BranchRepository branchRepository,
        BranchMapper branchMapper,
        DomainMapper domainMapper,
        DomainService domainService
    ) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.domainMapper = domainMapper;
        this.domainService = domainService;
    }

    @Override
    public BranchDTO save(BranchDTO branchDTO) {
        log.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }

    @Override
    public Optional<BranchDTO> partialUpdate(BranchDTO branchDTO) {
        log.debug("Request to partially update Branch : {}", branchDTO);

        return branchRepository
            .findById(branchDTO.getId())
            .map(
                existingBranch -> {
                    branchMapper.partialUpdate(existingBranch, branchDTO);
                    return existingBranch;
                }
            )
            .map(branchRepository::save)
            .map(branchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> findAll() {
        log.debug("Request to get all Branches");
        LinkedList<BranchDTO> result = new LinkedList<BranchDTO>();
        branchRepository
            .findAll()
            .forEach(
                i -> {
                    result.add(branchMapper.toDto(i));
                }
            );
        return result;
        //return branchRepository.findAll().stream().map(branchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BranchDTO> findOne(Long id) {
        log.debug("Request to get Branch : {}", id);
        return branchRepository.findById(id).map(branchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Branch : {}", id);
        branchRepository.deleteById(id);
    }

    @Override
    public List<BranchDTO> findByDomain(Long id, int page, int size) {
        log.debug("Request to get Branch by domain id: {}", id);
        Optional<DomainDTO> b = domainService.findOne(id);
        Pageable paging = PageRequest.of(page, size);
        if (b.isPresent()) {
            return branchRepository
                .findByDomain(domainMapper.toEntity(b.get()), paging)
                .stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.error("Invalid branch ID: {}", id);
        return null;
    }
}
