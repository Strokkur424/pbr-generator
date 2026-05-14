package net.strokkur.pbr.map;

import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;

public interface NormalMap extends CacheSerializable {

  static NormalMap fromTexture(TextureSource texture) {
    throw new IllegalStateException("TODO");
  }

  static NormalMap fromAtlas(TextureAtlasSource atlas) {
    throw new IllegalStateException("TODO");
  }
}
