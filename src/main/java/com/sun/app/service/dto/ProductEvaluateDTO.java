package com.sun.app.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.sun.app.domain.ProductEvaluate} entity.
 */
public class ProductEvaluateDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer point;

    private String evaluate;

    @NotNull
    private Instant createdAt;

    private Long userId;

    private Long productId;

//    public UserDTO getUser() {
//        return user;
//    }
//
//    public void setUser(UserDTO user) {
//        this.user = user;
//    }

//    public ProductDTO getProduct() {
//        return product;
//    }

//    public void setProduct(ProductDTO product) {
//        this.product = product;
//    }

//    private UserDTO user;

//    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductEvaluateDTO productEvaluateDTO = (ProductEvaluateDTO) o;
        if (productEvaluateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productEvaluateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductEvaluateDTO{" +
            "id=" + getId() +
            ", point=" + getPoint() +
            ", evaluate='" + getEvaluate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
//            ", product=" + getProduct() +
//            ", user=" + getUser() +
            "}";
    }
}
