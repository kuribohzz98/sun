package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.ProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductTypeMapper.class, ProviderMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Mapping(source = "productType.id", target = "productTypeId")
    @Mapping(source = "provider.id", target = "providerId")
    ProductDTO toDto(Product product);

    @Mapping(source = "productTypeId", target = "productType")
    @Mapping(source = "providerId", target = "provider")
    Product toEntity(ProductDTO productDTO);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
