package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.ContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Content} and its DTO {@link ContentDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface ContentMapper extends EntityMapper<ContentDTO, Content> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    ContentDTO toDto(Content s);
}
