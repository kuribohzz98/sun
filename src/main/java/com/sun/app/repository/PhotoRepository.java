package com.sun.app.repository;

import com.sun.app.domain.Photo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Photo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> getPhotoByName(String name);
}
