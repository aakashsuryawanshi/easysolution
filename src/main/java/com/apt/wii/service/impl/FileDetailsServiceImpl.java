package com.apt.wii.service.impl;

import com.apt.wii.domain.FileDetails;
import com.apt.wii.repository.FileDetailsRepository;
import com.apt.wii.service.FileDetailsService;
import com.apt.wii.service.dto.FileDetailsDTO;
import com.apt.wii.service.mapper.FileDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileDetails}.
 */
@Service
@Transactional
public class FileDetailsServiceImpl implements FileDetailsService {

    private final Logger log = LoggerFactory.getLogger(FileDetailsServiceImpl.class);

    private final FileDetailsRepository fileDetailsRepository;

    private final FileDetailsMapper fileDetailsMapper;

    public FileDetailsServiceImpl(FileDetailsRepository fileDetailsRepository, FileDetailsMapper fileDetailsMapper) {
        this.fileDetailsRepository = fileDetailsRepository;
        this.fileDetailsMapper = fileDetailsMapper;
    }

    @Override
    public FileDetailsDTO save(FileDetailsDTO fileDetailsDTO) {
        log.debug("Request to save FileDetails : {}", fileDetailsDTO);
        FileDetails fileDetails = fileDetailsMapper.toEntity(fileDetailsDTO);
        fileDetails = fileDetailsRepository.save(fileDetails);
        return fileDetailsMapper.toDto(fileDetails);
    }

    @Override
    public Optional<FileDetailsDTO> partialUpdate(FileDetailsDTO fileDetailsDTO) {
        log.debug("Request to partially update FileDetails : {}", fileDetailsDTO);

        return fileDetailsRepository
            .findById(fileDetailsDTO.getId())
            .map(
                existingFileDetails -> {
                    fileDetailsMapper.partialUpdate(existingFileDetails, fileDetailsDTO);
                    return existingFileDetails;
                }
            )
            .map(fileDetailsRepository::save)
            .map(fileDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDetailsDTO> findAll() {
        log.debug("Request to get all FileDetails");
        return fileDetailsRepository.findAll().stream().map(fileDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDetailsDTO> findOne(Long id) {
        log.debug("Request to get FileDetails : {}", id);
        return fileDetailsRepository.findById(id).map(fileDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileDetails : {}", id);
        fileDetailsRepository.deleteById(id);
    }
}
