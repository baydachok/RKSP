package org.example.images.services;

import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
import org.example.images.dto.ImageFormDto;
import org.example.images.models.ImageEntity;
import org.example.images.repositories.ImageRepository;
import org.example.images.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private static final List<String> AVAILABLE_FILE_MEDIA_TYPES = List.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);

  private final ImageRepository imageRepository;

  @Autowired
  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  @Transactional
  public ImageEntity saveImage(ImageFormDto imageFormDto) throws IOException {
    if (!FileUtils.isValidMediaType(imageFormDto.getImage(), AVAILABLE_FILE_MEDIA_TYPES)) {
      throw new BadRequestException("Bad file media type");
    }
    if (imageRepository.findByImageName(imageFormDto.getName()) != null) {
      throw new BadRequestException("Image name already exist");
    }
    return imageRepository.save(new ImageEntity(imageFormDto.getName(), imageFormDto.getImage().getBytes()));
  }

  @Transactional
  public ByteArrayInputStream getImage(String imageName) throws IOException {
    ImageEntity imageEntity = imageRepository.findByImageName(imageName);
    if (imageEntity == null) {
      throw new FileNotFoundException("Image with specified name not found");
    }
    return new ByteArrayInputStream(imageEntity.getImageBytes());
  }

  @Transactional
  public void deleteImage(String imageName) {
    Optional.ofNullable(imageRepository.findByImageName(imageName))
        .ifPresent(imageRepository::delete);
  }

}
