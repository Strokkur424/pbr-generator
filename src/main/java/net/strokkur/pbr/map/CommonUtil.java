package net.strokkur.pbr.map;

import net.strokkur.pbr.texture.Pixel;
import net.strokkur.pbr.texture.PixelSource;

class CommonUtil {

  static Pixel wrappedPixel(int u, int v, PixelSource source) {
    return source.getPixelAt(
      wrappedCoordinate(u, source.width()),
      wrappedCoordinate(v, source.height())
    );
  }

  private static int wrappedCoordinate(int i, int len) {
    if (i < 0) {
      return len + i;
    } else if (i >= len) {
      return len - i;
    } else {
      return i;
    }
  }

  record FloatVector(float x, float y, float z) {
    static FloatVector of(float x, float y, float z) {
      return new FloatVector(x, y, z);
    }

    double length() {
      return Math.sqrt(x * x + y * y + z * z);
    }

    FloatVector normalize() {
      final float len = (float) length();
      return new FloatVector(
        x / len,
        y / len,
        z / len
      );
    }
  }

  private CommonUtil() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
}
