package com.apt.wii.service.impl;

import com.apt.wii.domain.Content;
import com.apt.wii.repository.ContentRepository;
import com.apt.wii.service.ContentService;
import com.apt.wii.service.QuestionService;
import com.apt.wii.service.dto.BranchDTO;
import com.apt.wii.service.dto.ContentDTO;
import com.apt.wii.service.dto.QuestionDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.mapper.ContentMapper;
import com.apt.wii.service.mapper.QuestionMapper;
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
 * Service Implementation for managing {@link Content}.
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    private final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final ContentRepository contentRepository;

    private final ContentMapper contentMapper;

    private final QuestionMapper questionMapper;

    private final QuestionService questionService;

    public ContentServiceImpl(
        ContentRepository contentRepository,
        ContentMapper contentMapper,
        QuestionService questionService,
        QuestionMapper questionMapper
    ) {
        this.contentRepository = contentRepository;
        this.contentMapper = contentMapper;
        this.questionMapper = questionMapper;
        this.questionService = questionService;
    }

    @Override
    public ContentDTO save(ContentDTO contentDTO) {
        log.debug("Request to save Content : {}", contentDTO);
        Content content = contentMapper.toEntity(contentDTO);
        content = contentRepository.save(content);
        return contentMapper.toDto(content);
    }

    @Override
    public Optional<ContentDTO> partialUpdate(ContentDTO contentDTO) {
        log.debug("Request to partially update Content : {}", contentDTO);

        return contentRepository
            .findById(contentDTO.getId())
            .map(
                existingContent -> {
                    contentMapper.partialUpdate(existingContent, contentDTO);
                    return existingContent;
                }
            )
            .map(contentRepository::save)
            .map(contentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentDTO> findAll() {
        log.debug("Request to get all Contents");
        LinkedList<ContentDTO> result = new LinkedList<ContentDTO>();
        contentRepository
            .findAll()
            .forEach(
                i -> {
                    result.add(contentMapper.toDto(i));
                }
            );
        return result;
        //return contentRepository.findAll().stream().map(contentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentDTO> findOne(Long id) {
        log.debug("Request to get Content : {}", id);
        return contentRepository.findById(id).map(contentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Content : {}", id);
        contentRepository.deleteById(id);
    }

    @Override
    public List<ContentDTO> findByQuestion(Long id) {
        log.debug("Request to get content by question id: {}", id);
        Optional<QuestionDTO> b = questionService.findOne(id);

        if (b.isPresent()) {
            return contentRepository
                .findByQuestion(questionMapper.toEntity(b.get()))
                .stream()
                .map(contentMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.error("Invalid question ID: {}", id);
        return null;
    }
}
