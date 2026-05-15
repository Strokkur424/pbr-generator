package net.strokkur.pbr.map;

import net.strokkur.pbr.texture.Pixel;
import net.strokkur.pbr.texture.PixelSource;

import static net.strokkur.pbr.map.CommonUtil.wrappedPixel;

record SpecularMapImpl(
  byte[] data,
  int width,
  int height
) implements SpecularMap {

  private static byte specularValueFor(Pixel pixel) {
    final double value = Math.clamp(
      -Math.log(-pixel.luminance() + 1.3),
      0, 1
    );
    return (byte) (value * 255);
  }

  static byte[] dataFor(PixelSource pixelSource) {
    final byte[] out = new byte[pixelSource.width() * pixelSource.height()];
    for (int u = 0; u < pixelSource.width(); u++) {
      for (int v = 0; v < pixelSource.height(); v++) {
        out[u + v * pixelSource.width()] = specularValueFor(wrappedPixel(u, v, pixelSource));
      }
    }
    return out;
  }
}
