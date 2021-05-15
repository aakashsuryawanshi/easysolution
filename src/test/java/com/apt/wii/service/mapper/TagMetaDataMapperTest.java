package com.apt.wii.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagMetaDataMapperTest {

    private TagMetaDataMapper tagMetaDataMapper;

    @BeforeEach
    public void setUp() {
        tagMetaDataMapper = new TagMetaDataMapperImpl();
    }
}
