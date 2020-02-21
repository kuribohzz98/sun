package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.UserDeliveryInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDeliveryInfo} and its DTO {@link UserDeliveryInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserDeliveryInfoMapper extends EntityMapper<UserDeliveryInfoDTO, UserDeliveryInfo> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserDeliveryInfoDTO toDto(UserDeliveryInfo userDeliveryInfo);

    @Mapping(source = "userId", target = "user")
    UserDeliveryInfo toEntity(UserDeliveryInfoDTO userDeliveryInfoDTO);

    default UserDeliveryInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserDeliveryInfo userDeliveryInfo = new UserDeliveryInfo();
        userDeliveryInfo.setId(id);
        return userDeliveryInfo;
    }
}
