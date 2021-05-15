package com.apt.wii.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemesterMapperTest {

    private SemesterMapper semesterMapper;

    @BeforeEach
    public void setUp() {
        semesterMapper = new SemesterMapperImpl();
    }
}
