package com.apt.wii.service.impl;

import com.apt.wii.domain.Subject;
import com.apt.wii.repository.SubjectRepository;
import com.apt.wii.service.BranchService;
import com.apt.wii.service.SemesterService;
import com.apt.wii.service.SubjectService;
import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.SemesterDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.mapper.SemesterMapper;
import com.apt.wii.service.mapper.SubjectMapper;
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
 * Service Implementation for managing {@link Subject}.
 */
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final SubjectRepository subjectRepository;

    private final SubjectMapper subjectMapper;

    private final SemesterMapper semesterMapper;

    private final SemesterService semesterService;

    public SubjectServiceImpl(
        SemesterService semesterService,
        SemesterMapper semesterMapper,
        SubjectRepository subjectRepository,
        SubjectMapper subjectMapper
    ) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
        this.semesterService = semesterService;
        this.semesterMapper = semesterMapper;
    }

    @Override
    public SubjectDTO save(SubjectDTO subjectDTO) {
        log.debug("Request to save Subject : {}", subjectDTO);
        Subject subject = subjectMapper.toEntity(subjectDTO);
        subject = subjectRepository.save(subject);
        return subjectMapper.toDto(subject);
    }

    @Override
    public Optional<SubjectDTO> partialUpdate(SubjectDTO subjectDTO) {
        log.debug("Request to partially update Subject : {}", subjectDTO);

        return subjectRepository
            .findById(subjectDTO.getId())
            .map(
                existingSubject -> {
                    subjectMapper.partialUpdate(existingSubject, subjectDTO);
                    return existingSubject;
                }
            )
            .map(subjectRepository::save)
            .map(subjectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectDTO> findAll() {
        log.debug("Request to get all Subjects");
        LinkedList<SubjectDTO> result = new LinkedList<SubjectDTO>();
        subjectRepository
            .findAll()
            .forEach(
                i -> {
                    result.add(subjectMapper.toDto(i));
                }
            );
        return result;
        //return subjectRepository.findAll().stream().map(subjectMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectDTO> findOne(Long id) {
        log.debug("Request to get Subject : {}", id);
        return subjectRepository.findById(id).map(subjectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subject : {}", id);
        subjectRepository.deleteById(id);
    }

    @Override
    public List<SubjectDTO> findBySemester(Long semesterId) {
        log.debug("Request to get Semester by branch id: {}", semesterId);
        Optional<SemesterDTO> b = semesterService.findOne(semesterId);
        if (b.isPresent()) {
            return subjectRepository
                .findBySemester(semesterMapper.toEntity(b.get()))
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.error("Invalid branch ID: {}", semesterId);
        return null;
    }
}
