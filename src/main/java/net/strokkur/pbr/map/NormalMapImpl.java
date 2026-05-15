package net.strokkur.pbr.map;

import net.strokkur.pbr.map.CommonUtil.FloatVector;
import net.strokkur.pbr.texture.PixelSource;

import static net.strokkur.pbr.map.CommonUtil.wrappedPixel;

record NormalMapImpl(
  int[] rgbaData,
  int width,
  int height
) implements NormalMap {
  private static final float SOBEL_STRENGTH = 0.35f;

  static int[] rgbaDataFor(PixelSource pixelSource) {
    final int[] data = new int[pixelSource.width() * pixelSource.height()];
    for (int u = 0; u < pixelSource.width(); u++) {
      for (int v = 0; v < pixelSource.height(); v++) {
        data[u + v * pixelSource.width()] = sobelFilterAt(u, v, pixelSource);
      }
    }

    return data;
  }

  private static float intensityAt(int u, int v, PixelSource pixelSource) {
    return wrappedPixel(u, v, pixelSource).luminance();
  }

  private static int transformComponent(float value) {
    return (int) ((value + 1) * (255f / 2f));
  }

  private static int sobelFilterAt(int u, int v, PixelSource pixelSource) {
    final float topLeft = intensityAt(u - 1, v - 1, pixelSource);
    final float top = intensityAt(u, v - 1, pixelSource);
    final float topRight = intensityAt(u + 1, v - 1, pixelSource);
    final float left = intensityAt(u - 1, v, pixelSource);
    final float right = intensityAt(u + 1, v, pixelSource);
    final float bottomLeft = intensityAt(u - 1, v + 1, pixelSource);
    final float bottom = intensityAt(u, v + 1, pixelSource);
    final float bottomRight = intensityAt(u + 1, v + 1, pixelSource);

    final float dx = (topRight + 2.0f * right + bottomRight) - (topLeft + 2.0f * left + bottomLeft);
    final float dy = (bottomLeft + 2.0f * bottom + bottomRight) - (topLeft + 2.0f * top + topRight);
    final float dz = 1.0f / SOBEL_STRENGTH;

    final FloatVector normal = FloatVector.of(dx, dy, dz).normalize();
    return 0xFF
      | transformComponent(normal.x()) << 24
      | transformComponent(normal.y()) << 16
      | transformComponent(normal.z()) << 8;
  }

  @Override
  public boolean shouldCachePersistently() {
    return width > 64 && height > 64;
  }
}
