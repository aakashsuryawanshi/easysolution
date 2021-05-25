package com.apt.wii.service.impl;

import com.apt.wii.domain.FileDetails;
import com.apt.wii.domain.FileMetaData;
import com.apt.wii.repository.FileMetaDataRepository;
import com.apt.wii.service.FileDetailsService;
import com.apt.wii.service.FileMetaDataService;
import com.apt.wii.service.dto.FileDetailsDTO;
import com.apt.wii.service.dto.FileMetaDataDTO;
import com.apt.wii.service.mapper.FileDetailsMapper;
import com.apt.wii.service.mapper.FileMetaDataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileMetaData}.
 */
@Service
@Transactional
public class FileMetaDataServiceImpl implements FileMetaDataService {

    private final Logger log = LoggerFactory.getLogger(FileMetaDataServiceImpl.class);

    private final FileMetaDataRepository fileMetaDataRepository;

    private final FileMetaDataMapper fileMetaDataMapper;

    private final FileDetailsService fileDetailsService;

    private final FileDetailsMapper fileDetailsMapper;

    public FileMetaDataServiceImpl(
        FileMetaDataRepository fileMetaDataRepository,
        FileMetaDataMapper fileMetaDataMapper,
        FileDetailsService fileDetailsService,
        FileDetailsMapper fileDetailsMapper
    ) {
        this.fileMetaDataRepository = fileMetaDataRepository;
        this.fileMetaDataMapper = fileMetaDataMapper;
        this.fileDetailsService = fileDetailsService;
        this.fileDetailsMapper = fileDetailsMapper;
    }

    @Override
    public FileMetaDataDTO save(FileMetaDataDTO fileMetaDataDTO) {
        log.debug("Request to save FileMetaData : {}", fileMetaDataDTO);
        FileMetaData fileMetaData = fileMetaDataMapper.toEntity(fileMetaDataDTO);
        fileMetaData = fileMetaDataRepository.save(fileMetaData);
        return fileMetaDataMapper.toDto(fileMetaData);
    }

    @Override
    public Optional<FileMetaDataDTO> partialUpdate(FileMetaDataDTO fileMetaDataDTO) {
        log.debug("Request to partially update FileMetaData : {}", fileMetaDataDTO);

        return fileMetaDataRepository
            .findById(fileMetaDataDTO.getId())
            .map(
                existingFileMetaData -> {
                    fileMetaDataMapper.partialUpdate(existingFileMetaData, fileMetaDataDTO);
                    return existingFileMetaData;
                }
            )
            .map(fileMetaDataRepository::save)
            .map(fileMetaDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileMetaDataDTO> findAll() {
        log.debug("Request to get all FileMetaData");
        return fileMetaDataRepository.findAll().stream().map(fileMetaDataMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<FileMetaDataDTO> findByFileDetail(Long fileDetailsId) {
        Optional<FileDetailsDTO> fileDetailsDTO = fileDetailsService.findOne(fileDetailsId);
        if (fileDetailsDTO.isPresent()) {
            FileDetails fileDetails = fileDetailsMapper.toEntity(fileDetailsDTO.get());
            List<FileMetaData> fileMetaData = fileMetaDataRepository.findByFileDetails(fileDetails);
            return fileMetaDataMapper.toDto(fileMetaData);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileMetaDataDTO> findOne(Long id) {
        log.debug("Request to get FileMetaData : {}", id);
        return fileMetaDataRepository.findById(id).map(fileMetaDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileMetaData : {}", id);
        fileMetaDataRepository.deleteById(id);
    }
}
