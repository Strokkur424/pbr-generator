package net.strokkur.pbr.map;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;
import org.jetbrains.annotations.ApiStatus;

/// The normal map generation is based on the [Sobel Operator](https://en.wikipedia.org/wiki/Sobel_operator).
/// The code implementation is based on GManNickG's [stack overflow answer](https://stackoverflow.com/a/2368794).
public interface NormalMap extends CacheSerializable {

  /// You should call [PbrGen#getNormal(TextureSource)] instead of calling
  /// this method directly.
  @ApiStatus.Internal
  static NormalMap fromTexture(TextureSource texture) {
    return new NormalMapImpl(
      NormalMapImpl.rgbaDataFor(texture), texture.width(), texture.height()
    );
  }

  /// You should call [PbrGen#getNormal(TextureAtlasSource)] instead of calling
  /// this method directly.
  @ApiStatus.Internal
  static NormalMap fromAtlas(TextureAtlasSource atlas) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @ApiStatus.Internal
  static NormalMap direct(int[] array, int width, int height) {
    return new NormalMapImpl(array, width, height);
  }

  /// Gets the data of this normal map as an array of integers, where each
  /// integer holds the RGBA data of a pixel. The `alpha` value will always
  /// be `255` (`0xFF`).
  ///
  /// To retrieve the value for a specific pixel, use the following code:
  /// ```
  /// NormalMap normal = ...;
  /// int u = ...;
  /// int v = ...;
  ///
  /// int pixelData = normal.rgbaData()[u + v * normal.width()];
  /// ```
  /// Alternatively, you could directly use [#rgbaAt(int, int)].
  ///
  /// To retrieve a specific component (R, G, B, or A), you can use the following code.
  /// The cast to `byte` is not necessary
  /// ```
  /// int pixelData = ...;
  /// byte red = (byte) (pixelData >> 24 & 0xFF);
  /// byte green = (byte) (pixelData >> 16 & 0xF);
  /// byte blue = (byte) (pixelData >> 8 & 0xFF);
  /// byte alpha = (byte) (pixelData & 0xFF);
  /// ```
  ///
  /// @apiNote this method returns a **reference** to the internal `int[]` without copying.
  /// @see #rgbaAt(int, int)
  /// @see #width()
  /// @see #height()
  int[] rgbaData();

  /// The vertical height of this normal map.
  int height();

  /// The horizontal width of this normal map.
  int width();

  /// Gets the RGBA value at specific UV coordinates. This is equivalent to calling
  /// ```
  /// normalMap.rgbaData()[u + v * normalMap.width()];
  /// ```
  ///
  /// @see #rgbaData()
  /// @see #width()
  default int rgbaAt(int u, int v) {
    return rgbaData()[u + v * width()];
  }
}
