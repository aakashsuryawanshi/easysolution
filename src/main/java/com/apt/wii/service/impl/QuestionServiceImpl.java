package com.apt.wii.service.impl;

import com.apt.wii.domain.Question;
import com.apt.wii.domain.TagMetaData;
import com.apt.wii.repository.QuestionRepository;
import com.apt.wii.service.QuestionService;
import com.apt.wii.service.SemesterService;
import com.apt.wii.service.SubjectService;
import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.DomainDTO;
import com.apt.wii.service.dto.QuestionDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.dto.TagMetaDataDTO;
import com.apt.wii.service.mapper.QuestionMapper;
import com.apt.wii.service.mapper.SubjectMapper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.MapUtils;

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
        LinkedList<QuestionDTO> result = new LinkedList<QuestionDTO>();
        questionRepository
            .findAll()
            .forEach(
                i -> {
                    result.add(questionMapper.toDto(i));
                }
            );
        return result;
        // return
        // questionRepository.findAll().stream().map(questionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
    public Page<Question> findBySubject(Long subjectId, int page, int size, String title) {
        log.debug("Request to get questions by subject id: {}", subjectId);
        Optional<SubjectDTO> b = subjectService.findOne(subjectId);
        Pageable paging = PageRequest.of(page, size);
        if (b.isPresent()) {
            return StringUtils.isBlank(title)
                ? questionRepository.findBySubject(subjectMapper.toEntity(b.get()), paging)
                : questionRepository.findBySubjectAndTitleContainingIgnoreCase(subjectMapper.toEntity(b.get()), title, paging);
        }
        log.error("Invalid branch ID: {}", subjectId);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<Question> getQuestionBySubject(Long subjectId, Map<String, Object> tags, String title, int page, int size) {
        log.debug("Request to get questions by tags: {}", tags);
        Pageable paging = PageRequest.of(page, size);
        if (MapUtils.isEmpty(tags)) {
            return findBySubject(subjectId, page, size, title);
        }
        List<String> filterTags = new ArrayList<>();
        tags.values().stream().forEach(values -> filterTags.addAll((List<String>) values));
        return questionRepository.getQuestionsBySubjectAndTags(subjectId, tags.keySet(), filterTags, paging);
    }
}
