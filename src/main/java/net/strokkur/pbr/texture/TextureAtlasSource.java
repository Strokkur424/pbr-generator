package net.strokkur.pbr.texture;

import net.strokkur.pbr.exceptions.InvalidDimensionsException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public interface TextureAtlasSource extends XXH3Hashable {

  static TextureAtlasSource load(String key, SpriteDimension spriteDimension, URL url) throws IOException, InvalidDimensionsException {
    return new TextureAtlasSourceImpl(key, spriteDimension, ImageIO.read(url));
  }

  static TextureAtlasSource load(String key, SpriteDimension spriteDimension, File file) throws IOException, InvalidDimensionsException {
    return new TextureAtlasSourceImpl(key, spriteDimension, ImageIO.read(file));
  }

  static TextureAtlasSource load(String key, SpriteDimension spriteDimension, URI uri) throws IOException, InvalidDimensionsException {
    return load(key, spriteDimension, uri.toURL());
  }

  static TextureAtlasSource load(String key, SpriteDimension spriteDimension, Path path) throws IOException, InvalidDimensionsException {
    return load(key, spriteDimension, path.toFile());
  }

  static TextureAtlasSource of(String key, SpriteDimension spriteDimension, BufferedImage image) throws InvalidDimensionsException {
    return new TextureAtlasSourceImpl(key, spriteDimension, image);
  }

  /// The key to identify this atlas with.
  String key();

  /// The dimensions of an individual sprite. Must result in a clean number of sprites being retrievable
  /// from the source image.o
  SpriteDimension spriteDimension();

  /// Cuts the atlas up into individual pixel sources. This method does not
  /// do any copying of data; instead, the individual [PixelSource] objects
  /// reference a small part of the original image buffer.
  ///
  /// For performance reasons, the array returned is created in the constructor
  /// of the class and not cloned before being returned. Therefore, any operations
  /// on the array should be **strictly avoided**.
  PixelSource[] allSprites();
}
