package com.sun.app.repository;

import com.sun.app.domain.Product;
import com.sun.app.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findOneByCode(String code);

    Page<Product> findAllBySalePriceGreaterThan(Integer salePrice, Pageable pageable);

    Page<Product> findAllByProductTypeId(Integer productTypeId, Pageable pageable);

    Page<Product> findAllByProductTypeIdAndProviderId(Integer productTypeId, Integer providerId, Pageable pageable);

    Page<Product> findAllByProductTypeIdAndSellPriceBetween(
        Integer productTypeId,
        Long sellPriceStart,
        Long sellPriceEnd,
        Pageable pageable
    );

    Page<Product> findAllByProductTypeIdAndProviderIdAndSellPriceBetween(
        Integer productTypeId,
        Integer providerId,
        Long sellPriceStart,
        Long sellPriceEnd,
        Pageable pageable
    );
}
