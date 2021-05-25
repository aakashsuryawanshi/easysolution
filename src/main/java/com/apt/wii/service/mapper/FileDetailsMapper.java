package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.FileDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileDetails} and its DTO {@link FileDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileDetailsMapper extends EntityMapper<FileDetailsDTO, FileDetails> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FileDetailsDTO toDtoId(FileDetails fileDetails);
}
