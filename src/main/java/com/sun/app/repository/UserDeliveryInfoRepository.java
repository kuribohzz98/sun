package com.sun.app.repository;

import com.sun.app.domain.UserDeliveryInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserDeliveryInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDeliveryInfoRepository extends JpaRepository<UserDeliveryInfo, Long> {

}
