package org.example.images.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.example.images.dto.ImageFormDto;
import org.example.images.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

  private final ImageService imageService;

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping(value = "/upload", consumes = {MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Void> saveImage(ImageFormDto imageFormDto) throws URISyntaxException, IOException {
    return ResponseEntity
        .created(new URI("/images/" + imageService.saveImage(imageFormDto).getImageName()))
        .build();
  }

  @GetMapping(produces = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE})
  public ResponseEntity<InputStreamResource> getImage(@RequestParam String imageName) throws IOException {
    return ResponseEntity.ok(new InputStreamResource(imageService.getImage(imageName)));
  }

  @DeleteMapping()
  public ResponseEntity<Void> deleteImage(@RequestParam String imageName) {
    imageService.deleteImage(imageName);
    return ResponseEntity.ok().build();
  }

}
