package com.sun.app.repository;

import com.sun.app.domain.ProductEvaluate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ProductEvaluate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEvaluateRepository extends JpaRepository<ProductEvaluate, Long> {
    Page<ProductEvaluate> findAllByProductId(Long id, Pageable page);
}
