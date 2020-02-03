package com.sun.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class SpecificationsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specifications.class);
        Specifications specifications1 = new Specifications();
        specifications1.setId(1L);
        Specifications specifications2 = new Specifications();
        specifications2.setId(specifications1.getId());
        assertThat(specifications1).isEqualTo(specifications2);
        specifications2.setId(2L);
        assertThat(specifications1).isNotEqualTo(specifications2);
        specifications1.setId(null);
        assertThat(specifications1).isNotEqualTo(specifications2);
    }
}
