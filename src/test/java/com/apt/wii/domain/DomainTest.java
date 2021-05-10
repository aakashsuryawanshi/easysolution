package com.apt.wii.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.apt.wii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domain.class);
        Domain domain1 = new Domain();
        domain1.setId(1L);
        Domain domain2 = new Domain();
        domain2.setId(domain1.getId());
        assertThat(domain1).isEqualTo(domain2);
        domain2.setId(2L);
        assertThat(domain1).isNotEqualTo(domain2);
        domain1.setId(null);
        assertThat(domain1).isNotEqualTo(domain2);
    }
}
