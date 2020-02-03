package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.SpecificationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specifications} and its DTO {@link SpecificationsDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface SpecificationsMapper extends EntityMapper<SpecificationsDTO, Specifications> {

    @Mapping(source = "product.id", target = "productId")
    SpecificationsDTO toDto(Specifications specifications);

    @Mapping(source = "productId", target = "product")
    Specifications toEntity(SpecificationsDTO specificationsDTO);

    default Specifications fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specifications specifications = new Specifications();
        specifications.setId(id);
        return specifications;
    }
}
