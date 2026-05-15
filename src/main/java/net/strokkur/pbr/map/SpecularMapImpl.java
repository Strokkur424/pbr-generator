package net.strokkur.pbr.map;

import net.strokkur.pbr.texture.PixelSource;

record SpecularMapImpl(
  byte[] data,
  int width,
  int height
) implements SpecularMap {
  static byte[] dataFor(PixelSource pixelSource) {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
