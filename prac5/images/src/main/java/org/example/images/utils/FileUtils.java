package org.example.images.utils;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

  public static boolean isValidMediaType(MultipartFile multipartFile, List<String> availableMediaTypes) {
    return availableMediaTypes.contains(multipartFile.getContentType());
  }

}
