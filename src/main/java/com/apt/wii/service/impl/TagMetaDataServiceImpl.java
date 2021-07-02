package com.apt.wii.service.impl;

import com.apt.wii.domain.TagMetaData;
import com.apt.wii.repository.TagMetaDataRepository;
import com.apt.wii.service.QuestionService;
import com.apt.wii.service.TagMetaDataService;
import com.apt.wii.service.dto.QuestionDTO;
import com.apt.wii.service.dto.SubjectDTO;
import com.apt.wii.service.dto.TagMetaDataDTO;
import com.apt.wii.service.mapper.QuestionMapper;
import com.apt.wii.service.mapper.TagMetaDataMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Service Implementation for managing {@link TagMetaData}.
 */
@Service
@Transactional
public class TagMetaDataServiceImpl implements TagMetaDataService {

    private final Logger log = LoggerFactory.getLogger(TagMetaDataServiceImpl.class);

    private final TagMetaDataRepository tagMetaDataRepository;

    private final TagMetaDataMapper tagMetaDataMapper;

    private final QuestionMapper questionMapper;

    private final QuestionService questionService;

    public TagMetaDataServiceImpl(
        TagMetaDataRepository tagMetaDataRepository,
        TagMetaDataMapper tagMetaDataMapper,
        QuestionMapper questionMapper,
        QuestionService questionService
    ) {
        this.tagMetaDataRepository = tagMetaDataRepository;
        this.tagMetaDataMapper = tagMetaDataMapper;
        this.questionMapper = questionMapper;
        this.questionService = questionService;
    }

    @Override
    public TagMetaDataDTO save(TagMetaDataDTO tagMetaDataDTO) {
        log.debug("Request to save TagMetaData : {}", tagMetaDataDTO);
        TagMetaData tagMetaData = tagMetaDataMapper.toEntity(tagMetaDataDTO);
        tagMetaData = tagMetaDataRepository.save(tagMetaData);
        return tagMetaDataMapper.toDto(tagMetaData);
    }

    @Override
    public Optional<TagMetaDataDTO> partialUpdate(TagMetaDataDTO tagMetaDataDTO) {
        log.debug("Request to partially update TagMetaData : {}", tagMetaDataDTO);

        return tagMetaDataRepository
            .findById(tagMetaDataDTO.getId())
            .map(
                existingTagMetaData -> {
                    tagMetaDataMapper.partialUpdate(existingTagMetaData, tagMetaDataDTO);
                    return existingTagMetaData;
                }
            )
            .map(tagMetaDataRepository::save)
            .map(tagMetaDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagMetaDataDTO> findAll() {
        log.debug("Request to get all TagMetaData");
        LinkedList<TagMetaDataDTO> result = new LinkedList<TagMetaDataDTO>();
        tagMetaDataRepository
            .findAll()
            .forEach(
                i -> {
                    result.add(tagMetaDataMapper.toDto(i));
                }
            );
        return result;
        //return tagMetaDataRepository.findAll().stream().map(tagMetaDataMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagMetaDataDTO> findOne(Long id) {
        log.debug("Request to get TagMetaData : {}", id);
        return tagMetaDataRepository.findById(id).map(tagMetaDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TagMetaData : {}", id);
        tagMetaDataRepository.deleteById(id);
    }

    @Override
    public List<TagMetaDataDTO> findByQuestion(Long id) {
        log.debug("Request to get tag meta data by question id: {}", id);
        Optional<QuestionDTO> b = questionService.findOne(id);
        if (b.isPresent()) {
            return tagMetaDataRepository
                .findByQuestion(questionMapper.toEntity(b.get()))
                .stream()
                .map(tagMetaDataMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        log.error("Invalid question ID: {}", id);
        return null;
    }

    @Override
    public Map<String, List<String>> findAllUniqueTags() {
        Iterable<TagMetaData> tagMetadataIterable = tagMetaDataRepository.findAll();
        Iterator<TagMetaData> tagsIterator = tagMetadataIterable.iterator();
        Map<String, List<String>> tags = new HashMap<>();
        while (tagsIterator.hasNext()) {
            TagMetaData tagMetaData = tagsIterator.next();
            List<String> values = tags.get(tagMetaData.getKey());
            if (CollectionUtils.isEmpty(values)) values = new ArrayList<String>();
            if (!values.contains(tagMetaData.getValue())) values.add(tagMetaData.getValue());
            tags.put(tagMetaData.getKey(), values);
        }
        return tags;
    }
}
