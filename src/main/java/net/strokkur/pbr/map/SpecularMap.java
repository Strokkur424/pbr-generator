package net.strokkur.pbr.map;

import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;

public interface SpecularMap extends CacheSerializable {

  static SpecularMap fromTexture(TextureSource texture) {
    throw new IllegalStateException("TODO");
  }

  static SpecularMap fromAtlas(TextureAtlasSource atlas) {
    throw new IllegalStateException("TODO");
  }

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
}
