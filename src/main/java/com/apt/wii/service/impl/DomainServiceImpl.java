package com.apt.wii.service.impl;

import com.apt.wii.domain.Domain;
import com.apt.wii.repository.DomainRepository;
import com.apt.wii.service.DomainService;
import com.apt.wii.service.dto.DomainDTO;
import com.apt.wii.service.mapper.DomainMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Domain}.
 */
@Service
@Transactional
public class DomainServiceImpl implements DomainService {

    private final Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);

    private final DomainRepository domainRepository;

    private final DomainMapper domainMapper;

    public DomainServiceImpl(DomainRepository domainRepository, DomainMapper domainMapper) {
        this.domainRepository = domainRepository;
        this.domainMapper = domainMapper;
    }

    @Override
    public DomainDTO save(DomainDTO domainDTO) {
        log.debug("Request to save Domain : {}", domainDTO);
        Domain domain = domainMapper.toEntity(domainDTO);
        domain = domainRepository.save(domain);
        return domainMapper.toDto(domain);
    }

    @Override
    public Optional<DomainDTO> partialUpdate(DomainDTO domainDTO) {
        log.debug("Request to partially update Domain : {}", domainDTO);

        return domainRepository
            .findById(domainDTO.getId())
            .map(
                existingDomain -> {
                    domainMapper.partialUpdate(existingDomain, domainDTO);
                    return existingDomain;
                }
            )
            .map(domainRepository::save)
            .map(domainMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainDTO> findAll() {
        log.debug("Request to get all Domains");
        return domainRepository.findAll().stream().map(domainMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DomainDTO> findOne(Long id) {
        log.debug("Request to get Domain : {}", id);
        return domainRepository.findById(id).map(domainMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domain : {}", id);
        domainRepository.deleteById(id);
    }
}
