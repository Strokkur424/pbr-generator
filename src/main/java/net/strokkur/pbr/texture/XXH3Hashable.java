package net.strokkur.pbr.texture;

import net.openhft.hashing.LongHashFunction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

interface XXH3Hashable {
  long hashWithXXH3();

  static long hashBufferedImage(BufferedImage img) throws IOException {
    byte[] bytes;
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      ImageIO.write(img, "png", stream);
      bytes = stream.toByteArray();
    }
    return LongHashFunction.xx3().hashBytes(bytes);
  }
}
