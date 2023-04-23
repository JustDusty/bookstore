package com.yassine.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageUtils {
  public static byte[] getImageBytesFromUrl(String imageUrl) throws Exception {
    if (imageUrl == null || imageUrl.isEmpty())
      throw new IllegalArgumentException("Image URL is null or empty");
    URL url = new URL(imageUrl);
    BufferedImage img = ImageIO.read(url);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(img, "jpg", baos);
    return baos.toByteArray();
  }
}
