package net.strokkur.pbr.internal.serializer;

import net.strokkur.pbr.map.NormalMap;

// TODO: implement this
class NormalMapSerializer implements CacheSerializer<NormalMap> {
  static NormalMapSerializer INSTANCE = new NormalMapSerializer();

  @Override
  public byte[] serialize(NormalMap value) {
    return new byte[0];
  }

  @Override
  public NormalMap deserialize(byte[] bytes) {
    return null;
  }
}
