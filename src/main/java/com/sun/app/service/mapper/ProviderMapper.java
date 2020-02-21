package com.sun.app.service.mapper;

import com.sun.app.domain.Provider;
import com.sun.app.service.dto.ProviderDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Provider} and its DTO {@link ProviderDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProviderMapper extends EntityMapper<ProviderDTO, Provider> {

    ProviderDTO toDto(Provider provider);

    Provider toEntity(ProviderDTO providerDTO);

    default Provider fromId(Long id) {
        if (id == null) {
            return null;
        }
        Provider provider = new Provider();
        provider.setId(id);
        return provider;
    }
}
