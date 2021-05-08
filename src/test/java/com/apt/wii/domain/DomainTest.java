package com.apt.wii.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.apt.wii.web.rest.TestUtil;

public class DomainTest {

    @Test
    public void equalsVerifier() throws Exception {
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
