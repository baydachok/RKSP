package org.example.images.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageFormDto {

  private String name;
  private MultipartFile image;

}
