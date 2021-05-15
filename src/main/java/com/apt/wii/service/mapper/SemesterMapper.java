package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.SemesterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semester} and its DTO {@link SemesterDTO}.
 */
@Mapper(componentModel = "spring", uses = { BranchMapper.class })
public interface SemesterMapper extends EntityMapper<SemesterDTO, Semester> {
    @Mapping(target = "branch", source = "branch", qualifiedByName = "id")
    SemesterDTO toDto(Semester s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SemesterDTO toDtoId(Semester semester);
}
