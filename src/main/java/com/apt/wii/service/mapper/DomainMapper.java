package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.DomainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Domain} and its DTO {@link DomainDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DomainMapper extends EntityMapper<DomainDTO, Domain> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DomainDTO toDtoId(Domain domain);
}
