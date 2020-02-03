package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.ProductHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductHistory} and its DTO {@link ProductHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductHistoryMapper extends EntityMapper<ProductHistoryDTO, ProductHistory> {

    @Mapping(source = "product.id", target = "productId")
    ProductHistoryDTO toDto(ProductHistory productHistory);

    @Mapping(source = "productId", target = "product")
    ProductHistory toEntity(ProductHistoryDTO productHistoryDTO);

    default ProductHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductHistory productHistory = new ProductHistory();
        productHistory.setId(id);
        return productHistory;
    }
}
