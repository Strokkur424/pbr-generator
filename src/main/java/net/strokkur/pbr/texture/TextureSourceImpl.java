package net.strokkur.pbr.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

final class TextureSourceImpl implements TextureSource {
  private final String key;
  private final BufferedImage bufferedImage;
  private final long xx3Hash;

  TextureSourceImpl(String key, BufferedImage bufferedImage) {
    // Ensure the key contains no (back)slash characters.
    if (key.contains("/") || key.contains("\\")) {
      throw new IllegalArgumentException("The key must not contained and (back)slash characters.");
    }

    this.key = key;
    this.bufferedImage = bufferedImage;
    try {
      this.xx3Hash = XXH3Hashable.hashBufferedImage(bufferedImage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public int width() {
    return bufferedImage.getWidth();
  }

  @Override
  public int height() {
    return bufferedImage.getHeight();
  }

  @Override
  public long hashWithXXH3() {
    return xx3Hash;
  }

  @Override
  public Pixel getPixelAt(int u, int v) throws IndexOutOfBoundsException {
    if (u < 0 || u >= width()) {
      throw new IndexOutOfBoundsException("The u coordinate %d is out of bounds for width %d".formatted(u, width()));
    } else if (v < 0 || v >= height()) {
      throw new IndexOutOfBoundsException("The v coordinate %d is out of bounds for height %d".formatted(v, height()));
    }

    return new Pixel(bufferedImage.getRGB(u, v));
  }
}
