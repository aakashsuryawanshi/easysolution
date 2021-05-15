package com.apt.wii.service.impl;

import com.apt.wii.domain.Semester;
import com.apt.wii.repository.SemesterRepository;
import com.apt.wii.service.SemesterService;
import com.apt.wii.service.dto.SemesterDTO;
import com.apt.wii.service.mapper.SemesterMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Semester}.
 */
@Service
@Transactional
public class SemesterServiceImpl implements SemesterService {

    private final Logger log = LoggerFactory.getLogger(SemesterServiceImpl.class);

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    public SemesterServiceImpl(SemesterRepository semesterRepository, SemesterMapper semesterMapper) {
        this.semesterRepository = semesterRepository;
        this.semesterMapper = semesterMapper;
    }

    @Override
    public SemesterDTO save(SemesterDTO semesterDTO) {
        log.debug("Request to save Semester : {}", semesterDTO);
        Semester semester = semesterMapper.toEntity(semesterDTO);
        semester = semesterRepository.save(semester);
        return semesterMapper.toDto(semester);
    }

    @Override
    public Optional<SemesterDTO> partialUpdate(SemesterDTO semesterDTO) {
        log.debug("Request to partially update Semester : {}", semesterDTO);

        return semesterRepository
            .findById(semesterDTO.getId())
            .map(
                existingSemester -> {
                    semesterMapper.partialUpdate(existingSemester, semesterDTO);
                    return existingSemester;
                }
            )
            .map(semesterRepository::save)
            .map(semesterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SemesterDTO> findAll() {
        log.debug("Request to get all Semesters");
        return semesterRepository.findAll().stream().map(semesterMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SemesterDTO> findOne(Long id) {
        log.debug("Request to get Semester : {}", id);
        return semesterRepository.findById(id).map(semesterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Semester : {}", id);
        semesterRepository.deleteById(id);
    }
}
