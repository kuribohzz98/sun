package com.sun.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductHistoryMapperTest {

    private ProductHistoryMapper productHistoryMapper;

    @BeforeEach
    public void setUp() {
        productHistoryMapper = new ProductHistoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productHistoryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productHistoryMapper.fromId(null)).isNull();
    }
}
