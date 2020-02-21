package com.sun.app.service.dto;
import com.sun.app.domain.ProductEvaluate;
import com.sun.app.domain.ProductHistory;
import com.sun.app.domain.Specifications;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.sun.app.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer productTypeId;

    @NotNull
    private Integer providerId;

    @NotNull
    private String name;

    private String code;

    @NotNull
    private Long sellPrice;

    @NotNull
    private Long importPrice;

    @NotNull
    private Integer quantity;

    private String productLine;

    private String image;

    private Integer salePrice;

    private Instant createdAt;

    private Instant updatedAt;

    private ProductTypeDTO productType;

    private ProviderDTO provider;

    private Set<ProductEvaluateDTO> productEvaluates = new HashSet<>();

    private Set<ProductHistoryDTO> productHistories = new HashSet<>();

    private Set<SpecificationsDTO> specifications = new HashSet<>();

    public ProductTypeDTO getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeDTO productType) {
        this.productType = productType;
    }

    public ProviderDTO getProvider() {
        return provider;
    }

    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(Long importPrice) {
        this.importPrice = importPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public Set<ProductEvaluateDTO> getProductEvaluates() {
        return productEvaluates;
    }

    public void setProductEvaluates(Set<ProductEvaluateDTO> productEvaluates) {
        this.productEvaluates = productEvaluates;
    }

    public Set<ProductHistoryDTO> getProductHistories() {
        return productHistories;
    }

    public void setProductHistories(Set<ProductHistoryDTO> productHistories) {
        this.productHistories = productHistories;
    }

    public Set<SpecificationsDTO> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Set<SpecificationsDTO> specifications) {
        this.specifications = specifications;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", productTypeId=" + getProductTypeId() +
            ", providerId=" + getProviderId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", sellPrice=" + getSellPrice() +
            ", importPrice=" + getImportPrice() +
            ", quantity=" + getQuantity() +
            ", productLine='" + getProductLine() + "'" +
            ", image='" + getImage() + "'" +
            ", salePrice=" + getSalePrice() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
