package net.strokkur.pbr.texture;

import net.strokkur.pbr.exceptions.InvalidDimensionsException;

import java.awt.image.BufferedImage;
import java.io.IOException;

final class TextureAtlasSourceImpl implements TextureAtlasSource {
  private final String key;
  private final SpriteDimension dimension;
  private final PixelSource[] splitSources;
  private final int splitSourcesWidth;
  private final int splitSourcesHeight;
  private final long xx3Hash;

  TextureAtlasSourceImpl(String key, SpriteDimension dimension, BufferedImage bufferedImage) throws InvalidDimensionsException {
    // Ensure the key contains no (back)slash characters.
    if (key.contains("/") || key.contains("\\")) {
      throw new IllegalArgumentException("The key must not contained and (back)slash characters.");
    }

    this.key = key;
    this.dimension = dimension;

    // Image width/height -- my future self will hate me for these variable names
    final int iw = bufferedImage.getWidth();
    final int ih = bufferedImage.getHeight();

    // Sprite dimensions width/height
    final int dw = dimension.x();
    final int dh = dimension.y();

    if (iw % dw != 0 || ih % dh != 0) {
      throw new InvalidDimensionsException(
        "Image of (%d, %d) [width, height] cannot be split up into %dx%d sprites.".formatted(iw, ih, dw, dh)
      );
    }

    final int spriteCountX = (iw / dw);
    final int spriteCountY = (ih / dh);

    int sourcesWidth = 0;
    int sourcesHeight = 0;

    final PixelSource[] sources = new PixelSource[spriteCountX * spriteCountY];
    for (int x = 0; x < iw; x += dw) {
      for (int y = 0; y < ih; y += dh) {
        sources[x + (spriteCountX * y)] = new SubImagePixelSource(
          bufferedImage, x * dw, y * dh, dw, dh
        );
        sourcesHeight++;
      }
      sourcesWidth++;
    }

    this.splitSources = sources;
    this.splitSourcesWidth = sourcesWidth;
    this.splitSourcesHeight = sourcesHeight;
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
  public SpriteDimension spriteDimension() {
    return dimension;
  }

  @Override
  public PixelSource[] allSprites() {
    return splitSources;
  }

  @Override
  public int spritesArrayWidth() {
    return splitSourcesWidth;
  }

  @Override
  public int spritesArrayHeight() {
    return splitSourcesHeight;
  }

  @Override
  public long hashWithXXH3() {
    return xx3Hash;
  }

  private record SubImagePixelSource(
    BufferedImage bigImage,
    int x, int y,
    int width, int height
  ) implements PixelSource {
    @Override
    public Pixel getPixelAt(int u, int v) throws IndexOutOfBoundsException {
      if (u < 0 || u >= width()) {
        throw new IndexOutOfBoundsException("The u coordinate %d is out of bounds for width %d".formatted(u, width()));
      } else if (v < 0 || v >= height()) {
        throw new IndexOutOfBoundsException("The v coordinate %d is out of bounds for height %d".formatted(v, height()));
      }
      return new Pixel(bigImage.getRGB(x + u, y + v));
    }
  }
}
