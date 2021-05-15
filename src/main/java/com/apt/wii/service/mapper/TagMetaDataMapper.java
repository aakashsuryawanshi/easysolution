package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.TagMetaDataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagMetaData} and its DTO {@link TagMetaDataDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface TagMetaDataMapper extends EntityMapper<TagMetaDataDTO, TagMetaData> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    TagMetaDataDTO toDto(TagMetaData s);
}
