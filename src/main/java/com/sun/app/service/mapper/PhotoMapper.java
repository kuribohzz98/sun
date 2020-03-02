package com.sun.app.service.mapper;

import com.sun.app.domain.*;
import com.sun.app.service.dto.PhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {



    default Photo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Photo photo = new Photo();
        photo.setId(id);
        return photo;
    }
}
