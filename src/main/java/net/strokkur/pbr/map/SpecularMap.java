package net.strokkur.pbr.map;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;
import org.jetbrains.annotations.ApiStatus;

import java.awt.image.BufferedImage;

/// This implementation of specular-map generation operates purely on luminance of the pixel. Dark pixels are treated
/// as matte, whilst lighter pixels are treated as reflective. For a less linear gradient, this implementation uses
/// a logarithmic function for luminance to specular mapping, resulting in more distinct values.
public interface SpecularMap extends CacheSerializable {

  /// You should call [PbrGen#getSpecular(TextureSource)] instead of calling
  /// this method directly.
  @ApiStatus.Internal
  static SpecularMap fromTexture(TextureSource texture) {
    return new SpecularMapImpl(
      SpecularMapImpl.dataFor(texture),
      texture.width(),
      texture.height()
    );
  }

  /// You should call [PbrGen#getSpecular(TextureAtlasSource)] instead of calling
  /// this method directly.
  @ApiStatus.Internal
  static SpecularMap fromAtlas(TextureAtlasSource atlas) {
    throw new IllegalStateException("TODO");
  }

  @ApiStatus.Internal
  static SpecularMap direct(byte[] array, int width, int height) {
    return new SpecularMapImpl(array, width, height);
  }

  /// Gets the data of this specular map as an array of bytes, where each
  /// byte holds the reflectiveness of a pixel (0-255).
  ///
  /// To retrieve the value for a specific pixel, use the following code:
  /// ```
  /// SpecularMap specular = ...;
  /// int u = ...;
  /// int v = ...;
  ///
  /// byte value = specular.data()[u + v * specular.width()];
  /// ```
  /// Alternatively, you could directly use [#valueAt(int, int)].
  ///
  /// @apiNote this method returns a **reference** to the internal `byte[]` without copying.
  /// @see #valueAt(int, int)
  /// @see #width()
  /// @see #height()
  byte[] data();

  /// The vertical height of this normal map.
  int height();

  /// The horizontal width of this normal map.
  int width();

  /// Gets the value at specific UV coordinates. This is equivalent to calling
  /// ```
  /// specularMap.data()[u + v * specularMap.width()];
  /// ```
  ///
  /// @see #data()
  /// @see #width()
  default byte valueAt(int u, int v) {
    return data()[u + v * width()];
  }

  /// Creates a [BufferedImage] out of this specular map.
  /// Useful for saving this texture as a viewable image to disk.s
  default BufferedImage toBufferedImage() {
    final BufferedImage img = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width(); x++) {
      for (int y = 0; y < height(); y++) {
        final byte value = valueAt(x, y);
        final int rgb = ((value & 0xFF) << 16) | ((value & 0xFF) << 8) | (value & 0xFF);
        img.setRGB(x, y, rgb);
      }
    }
    return img;
  }
}
