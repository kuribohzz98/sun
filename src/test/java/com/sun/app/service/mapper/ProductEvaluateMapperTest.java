package com.sun.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductEvaluateMapperTest {

    private ProductEvaluateMapper productEvaluateMapper;

    @BeforeEach
    public void setUp() {
        productEvaluateMapper = new ProductEvaluateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productEvaluateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productEvaluateMapper.fromId(null)).isNull();
    }
}
