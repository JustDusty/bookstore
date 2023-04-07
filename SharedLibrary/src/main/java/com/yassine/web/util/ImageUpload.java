package com.yassine.web.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUpload {
  public static final String UPLOAD_FOLDER =
      "C:\\Users\\HP\\Desktop\\PFE\\bookstore\\SharedLibrary\\src\\main\\resources\\static\\img";

  public boolean checkExisted(MultipartFile imageProduct) {
    boolean isExisted = false;
    try {
      File file = new File(UPLOAD_FOLDER + File.separator + imageProduct.getOriginalFilename());
      isExisted = file.exists();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isExisted;
  }



  public boolean saveImage(MultipartFile file) {
    boolean isUpload = false;
    try {
      Path saveTO = Paths.get(UPLOAD_FOLDER + File.separator + file.getOriginalFilename());
      Files.copy(file.getInputStream(), saveTO, StandardCopyOption.REPLACE_EXISTING);
      isUpload = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return isUpload;

  }


}
