package com.sun.app.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A ProductEvaluate.
 */
@Entity
@Table(name = "product_evaluate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotNull
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "evaluate")
    private String evaluate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPoint() {
        return point;
    }

    public ProductEvaluate point(Integer point) {
        this.point = point;
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public ProductEvaluate evaluate(String evaluate) {
        this.evaluate = evaluate;
        return this;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ProductEvaluate createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public ProductEvaluate user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public ProductEvaluate product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductEvaluate)) {
            return false;
        }
        return id != null && id.equals(((ProductEvaluate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductEvaluate{" +
            "id=" + getId() +
            ", point=" + getPoint() +
            ", evaluate='" + getEvaluate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
