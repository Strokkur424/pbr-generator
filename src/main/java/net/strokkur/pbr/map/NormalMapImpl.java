package net.strokkur.pbr.map;

import net.strokkur.pbr.texture.PixelSource;

record NormalMapImpl(
  int[] rgbaData,
  int width,
  int height
) implements NormalMap {
  static int[] rgbaDataFor(PixelSource pixelSource) {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
