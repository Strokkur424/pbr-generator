package net.strokkur.pbr.texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public interface TextureSource extends PixelSource, XXH3Hashable {

  static TextureSource load(String key, URL url) throws IOException {
    return new TextureSourceImpl(key, ImageIO.read(url));
  }

  static TextureSource load(String key, File file) throws IOException {
    return new TextureSourceImpl(key, ImageIO.read(file));
  }

  static TextureSource load(String key, URI uri) throws IOException {
    return load(key, uri.toURL());
  }

  static TextureSource load(String key, Path path) throws IOException {
    return load(key, path.toFile());
  }

  static TextureSource of(String key, BufferedImage image) {
    return new TextureSourceImpl(key, image);
  }

  /// The key to identify this texture with
  String key();
}
