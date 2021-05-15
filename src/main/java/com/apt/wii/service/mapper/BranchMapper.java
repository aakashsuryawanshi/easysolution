package com.apt.wii.service.mapper;

import com.apt.wii.domain.*;
import com.apt.wii.service.dto.BranchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Branch} and its DTO {@link BranchDTO}.
 */
@Mapper(componentModel = "spring", uses = { DomainMapper.class })
public interface BranchMapper extends EntityMapper<BranchDTO, Branch> {
    @Mapping(target = "domain", source = "domain", qualifiedByName = "id")
    BranchDTO toDto(Branch s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BranchDTO toDtoId(Branch branch);
}
