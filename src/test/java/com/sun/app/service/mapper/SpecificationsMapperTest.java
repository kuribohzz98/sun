package com.sun.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SpecificationsMapperTest {

    private SpecificationsMapper specificationsMapper;

    @BeforeEach
    public void setUp() {
        specificationsMapper = new SpecificationsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(specificationsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(specificationsMapper.fromId(null)).isNull();
    }
}
