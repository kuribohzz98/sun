package com.sun.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @NotNull
   @Column(name = "product_type_id", nullable = false)
   private Integer productTypeId;

    @NotNull
    @Column(name = "provider_id", nullable = false)
    private Integer providerId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "sell_price", nullable = false)
    private Long sellPrice;

    @NotNull
    @Column(name = "import_price", nullable = false)
    private Long importPrice;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "product_line")
    private String productLine;

    @Column(name = "image")
    private String image;

    @Column(name = "sale_price")
    private Integer salePrice;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_type_id", insertable = false, updatable = false)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "provider_id", insertable = false, updatable = false)
    private Provider provider;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "payment_product",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", insertable=false, updatable=false),
        inverseJoinColumns = @JoinColumn(name = "payment_id", referencedColumnName = "id", insertable=false, updatable=false))
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductEvaluate> productEvaluates = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<ProductHistory> productHistories = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Specifications> specifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   public Integer getProductTypeId() {
       return productTypeId;
   }

   public Product productTypeId(Integer productTypeId) {
       this.productTypeId = productTypeId;
       return this;
   }

   public void setProductTypeId(Integer productTypeId) {
       this.productTypeId = productTypeId;
   }

    public Integer getProviderId() {
        return providerId;
    }

    public Product providerId(Integer providerId) {
        this.providerId = providerId;
        return this;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public Product code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getSellPrice() {
        return sellPrice;
    }

    public Product sellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    public void setSellPrice(Long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getImportPrice() {
        return importPrice;
    }

    public Product importPrice(Long importPrice) {
        this.importPrice = importPrice;
        return this;
    }

    public void setImportPrice(Long importPrice) {
        this.importPrice = importPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductLine() {
        return productLine;
    }

    public Product productLine(String productLine) {
        this.productLine = productLine;
        return this;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getImage() {
        return image;
    }

    public Product image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public Product salePrice(Integer salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Product createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Product updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Product productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Provider getProvider() {
        return provider;
    }

    public Product provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Product addPayment(Payment payment) {
        this.payments.add(payment);
        payment.getProducts().add(this);
        return this;
    }

    public Product removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.getProducts().remove(this);
        return this;
    }

    public Set<ProductEvaluate> getProductEvaluates() {
        return productEvaluates;
    }

    public void setProductEvaluates(Set<ProductEvaluate> productEvaluates) {
        this.productEvaluates = productEvaluates;
    }

    public Set<ProductHistory> getProductHistories() {
        return productHistories;
    }

    public void setProductHistories(Set<ProductHistory> productHistories) {
        this.productHistories = productHistories;
    }

    public Set<Specifications> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Set<Specifications> specifications) {
        this.specifications = specifications;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Product{" +
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
            ", productType='" + getProductType() + "'" +
            ", provider='" + getProvider() + "'" +
//            ", specifications='" + getSpecifications() + "'" +
//            ", productEvaluates='" + getProductEvaluates() + "'" +
//            ", productHistories='" + getProductHistories() + "'" +
            "}";
    }
}
