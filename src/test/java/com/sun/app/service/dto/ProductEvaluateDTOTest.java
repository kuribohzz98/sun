package com.sun.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class ProductEvaluateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductEvaluateDTO.class);
        ProductEvaluateDTO productEvaluateDTO1 = new ProductEvaluateDTO();
        productEvaluateDTO1.setId(1L);
        ProductEvaluateDTO productEvaluateDTO2 = new ProductEvaluateDTO();
        assertThat(productEvaluateDTO1).isNotEqualTo(productEvaluateDTO2);
        productEvaluateDTO2.setId(productEvaluateDTO1.getId());
        assertThat(productEvaluateDTO1).isEqualTo(productEvaluateDTO2);
        productEvaluateDTO2.setId(2L);
        assertThat(productEvaluateDTO1).isNotEqualTo(productEvaluateDTO2);
        productEvaluateDTO1.setId(null);
        assertThat(productEvaluateDTO1).isNotEqualTo(productEvaluateDTO2);
    }
}
