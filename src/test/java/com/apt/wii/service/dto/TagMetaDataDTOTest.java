package com.apt.wii.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.apt.wii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagMetaDataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagMetaDataDTO.class);
        TagMetaDataDTO tagMetaDataDTO1 = new TagMetaDataDTO();
        tagMetaDataDTO1.setId(1L);
        TagMetaDataDTO tagMetaDataDTO2 = new TagMetaDataDTO();
        assertThat(tagMetaDataDTO1).isNotEqualTo(tagMetaDataDTO2);
        tagMetaDataDTO2.setId(tagMetaDataDTO1.getId());
        assertThat(tagMetaDataDTO1).isEqualTo(tagMetaDataDTO2);
        tagMetaDataDTO2.setId(2L);
        assertThat(tagMetaDataDTO1).isNotEqualTo(tagMetaDataDTO2);
        tagMetaDataDTO1.setId(null);
        assertThat(tagMetaDataDTO1).isNotEqualTo(tagMetaDataDTO2);
    }
}
