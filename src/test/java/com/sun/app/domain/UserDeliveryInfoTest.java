package com.sun.app.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sun.app.web.rest.TestUtil;

public class UserDeliveryInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDeliveryInfo.class);
        UserDeliveryInfo userDeliveryInfo1 = new UserDeliveryInfo();
        userDeliveryInfo1.setId(1L);
        UserDeliveryInfo userDeliveryInfo2 = new UserDeliveryInfo();
        userDeliveryInfo2.setId(userDeliveryInfo1.getId());
        assertThat(userDeliveryInfo1).isEqualTo(userDeliveryInfo2);
        userDeliveryInfo2.setId(2L);
        assertThat(userDeliveryInfo1).isNotEqualTo(userDeliveryInfo2);
        userDeliveryInfo1.setId(null);
        assertThat(userDeliveryInfo1).isNotEqualTo(userDeliveryInfo2);
    }
}
