package com.sun.app.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class UserDeliveryInfoMapperTest {

    private UserDeliveryInfoMapper userDeliveryInfoMapper;

    @BeforeEach
    public void setUp() {
        userDeliveryInfoMapper = new UserDeliveryInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(userDeliveryInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userDeliveryInfoMapper.fromId(null)).isNull();
    }
}
