package net.strokkur.pbr.texture;

public interface PixelSource {

  int width();

  int height();

  Pixel getPixelAt(int u, int v) throws IndexOutOfBoundsException;
}
