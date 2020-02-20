package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.ProductEvaluateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductEvaluate} and its DTO {@link ProductEvaluateDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface ProductEvaluateMapper extends EntityMapper<ProductEvaluateDTO, ProductEvaluate> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    ProductEvaluateDTO toDto(ProductEvaluate productEvaluate);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "productId", target = "product")
    ProductEvaluate toEntity(ProductEvaluateDTO productEvaluateDTO);

    default ProductEvaluate fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductEvaluate productEvaluate = new ProductEvaluate();
        productEvaluate.setId(id);
        return productEvaluate;
    }
}
