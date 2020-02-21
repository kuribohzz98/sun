package com.sun.app.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sun.app.domain.UserDeliveryInfo} entity.
 */
public class UserDeliveryInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer userId;

    @NotNull
    private String phone;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String ward;

    private String address;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDeliveryInfoDTO userDeliveryInfoDTO = (UserDeliveryInfoDTO) o;
        if (userDeliveryInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userDeliveryInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserDeliveryInfoDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", phone='" + getPhone() + "'" +
            ", city='" + getCity() + "'" +
            ", district='" + getDistrict() + "'" +
            ", ward='" + getWard() + "'" +
            ", address='" + getAddress() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
