package com.sun.app.repository;

import com.sun.app.domain.ProductEvaluate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductEvaluate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEvaluateRepository extends JpaRepository<ProductEvaluate, Long> {

}
