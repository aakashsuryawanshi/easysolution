package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.FileMetaDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileMetaData} and its DTO {@link FileMetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = { FileDetailsMapper.class })
public interface FileMetaDataMapper extends EntityMapper<FileMetaDataDTO, FileMetaData> {
    @Mapping(target = "fileDetails", source = "fileDetails", qualifiedByName = "id")
    FileMetaDataDTO toDto(FileMetaData s);
}
