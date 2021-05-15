package com.apt.wii.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DomainMapperTest {

    private DomainMapper domainMapper;

    @BeforeEach
    public void setUp() {
        domainMapper = new DomainMapperImpl();
    }
}
