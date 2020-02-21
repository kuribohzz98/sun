package com.sun.app.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class UserDeliveryInfoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDeliveryInfoDTO.class);
        UserDeliveryInfoDTO userDeliveryInfoDTO1 = new UserDeliveryInfoDTO();
        userDeliveryInfoDTO1.setId(1L);
        UserDeliveryInfoDTO userDeliveryInfoDTO2 = new UserDeliveryInfoDTO();
        assertThat(userDeliveryInfoDTO1).isNotEqualTo(userDeliveryInfoDTO2);
        userDeliveryInfoDTO2.setId(userDeliveryInfoDTO1.getId());
        assertThat(userDeliveryInfoDTO1).isEqualTo(userDeliveryInfoDTO2);
        userDeliveryInfoDTO2.setId(2L);
        assertThat(userDeliveryInfoDTO1).isNotEqualTo(userDeliveryInfoDTO2);
        userDeliveryInfoDTO1.setId(null);
        assertThat(userDeliveryInfoDTO1).isNotEqualTo(userDeliveryInfoDTO2);
    }
}
