package org.example.images.repositories;

import org.example.images.models.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

  ImageEntity findByImageName(String imageName);

}
