package com.sun.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class ProductEvaluateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductEvaluate.class);
        ProductEvaluate productEvaluate1 = new ProductEvaluate();
        productEvaluate1.setId(1L);
        ProductEvaluate productEvaluate2 = new ProductEvaluate();
        productEvaluate2.setId(productEvaluate1.getId());
        assertThat(productEvaluate1).isEqualTo(productEvaluate2);
        productEvaluate2.setId(2L);
        assertThat(productEvaluate1).isNotEqualTo(productEvaluate2);
        productEvaluate1.setId(null);
        assertThat(productEvaluate1).isNotEqualTo(productEvaluate2);
    }
}
