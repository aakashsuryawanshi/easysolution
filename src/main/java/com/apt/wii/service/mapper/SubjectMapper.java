package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.SubjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subject} and its DTO {@link SubjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { SemesterMapper.class })
public interface SubjectMapper extends EntityMapper<SubjectDTO, Subject> {
    @Mapping(target = "semester", source = "semester", qualifiedByName = "id")
    SubjectDTO toDto(Subject s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubjectDTO toDtoId(Subject subject);
}
