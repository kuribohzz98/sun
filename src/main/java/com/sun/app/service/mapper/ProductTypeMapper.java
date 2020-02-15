package com.sun.app.service.mapper;

import com.sun.app.domain.ProductType;
import com.sun.app.service.dto.ProductTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ProductType} and its DTO {@link ProductTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {

    ProductTypeDTO toDto(ProductType productType);

    ProductType toEntity(ProductTypeDTO productTypeDTO);

    default ProductType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductType productType = new ProductType();
        productType.setId(id);
        return productType;
    }
}
