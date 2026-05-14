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
}
