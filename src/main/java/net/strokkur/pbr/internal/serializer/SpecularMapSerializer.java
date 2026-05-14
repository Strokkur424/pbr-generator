package net.strokkur.pbr.internal.serializer;

import net.strokkur.pbr.map.SpecularMap;

// TODO: implement this
class SpecularMapSerializer implements CacheSerializer<SpecularMap> {
  static SpecularMapSerializer INSTANCE = new SpecularMapSerializer();

  @Override
  public byte[] serialize(SpecularMap value) {
    return new byte[0];
  }

  @Override
  public SpecularMap deserialize(byte[] bytes) {
    return null;
  }
}
