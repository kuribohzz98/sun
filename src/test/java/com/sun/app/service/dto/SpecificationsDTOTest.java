package com.sun.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class SpecificationsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecificationsDTO.class);
        SpecificationsDTO specificationsDTO1 = new SpecificationsDTO();
        specificationsDTO1.setId(1L);
        SpecificationsDTO specificationsDTO2 = new SpecificationsDTO();
        assertThat(specificationsDTO1).isNotEqualTo(specificationsDTO2);
        specificationsDTO2.setId(specificationsDTO1.getId());
        assertThat(specificationsDTO1).isEqualTo(specificationsDTO2);
        specificationsDTO2.setId(2L);
        assertThat(specificationsDTO1).isNotEqualTo(specificationsDTO2);
        specificationsDTO1.setId(null);
        assertThat(specificationsDTO1).isNotEqualTo(specificationsDTO2);
    }
}
