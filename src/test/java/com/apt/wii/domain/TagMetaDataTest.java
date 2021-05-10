package com.apt.wii.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.apt.wii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagMetaDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagMetaData.class);
        TagMetaData tagMetaData1 = new TagMetaData();
        tagMetaData1.setId(1L);
        TagMetaData tagMetaData2 = new TagMetaData();
        tagMetaData2.setId(tagMetaData1.getId());
        assertThat(tagMetaData1).isEqualTo(tagMetaData2);
        tagMetaData2.setId(2L);
        assertThat(tagMetaData1).isNotEqualTo(tagMetaData2);
        tagMetaData1.setId(null);
        assertThat(tagMetaData1).isNotEqualTo(tagMetaData2);
    }
}
