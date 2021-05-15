package com.apt.wii.service.impl;

import com.apt.wii.domain.Question;
import com.apt.wii.repository.QuestionRepository;
import com.apt.wii.service.QuestionService;
import com.apt.wii.service.SemesterService;
import com.apt.wii.service.SubjectService;
import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.QuestionDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.mapper.QuestionMapper;
import com.apt.wii.service.mapper.SubjectMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final SubjectMapper subjectMapper;

    private final SubjectService subjectService;

    public QuestionServiceImpl(
        SubjectService subjectService,
        QuestionRepository questionRepository,
        QuestionMapper questionMapper,
        SubjectMapper subjectMapper
    ) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    @Override
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(
                existingQuestion -> {
                    questionMapper.partialUpdate(existingQuestion, questionDTO);
                    return existingQuestion;
                }
            )
            .map(questionRepository::save)
            .map(questionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> findAll() {
        log.debug("Request to get all Questions");
        return questionRepository.findAll().stream().map(questionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }

    @Override
    public List<QuestionDTO> findBySubject(Long subjectId) {
        log.debug("Request to get Semester by branch id: {}", subjectId);
        Optional<SubjectDTO> b = subjectService.findOne(subjectId);
        if (b.isPresent()) {
            return questionRepository
                .findBySubject(subjectMapper.toEntity(b.get()))
                .stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.error("Invalid branch ID: {}", subjectId);
        return null;
    }
}
