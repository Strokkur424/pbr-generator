package net.strokkur.pbr.internal.serializer;

import net.strokkur.pbr.map.NormalMap;
import net.strokkur.pbr.map.SpecularMap;

/// A value serializer for use in persistent caches.
public interface CacheSerializer<T extends CacheSerializable> {

  /// Gets a serializer for [NormalMap] objects.
  static CacheSerializer<NormalMap> normal() {
    return NormalMapSerializer.INSTANCE;
  }

  /// Gets a serializer for [SpecularMap] objects.
  static CacheSerializer<SpecularMap> specular() {
    return SpecularMapSerializer.INSTANCE;
  }

  /// Serializes the given value to bytes.
  byte[] serialize(T value);

  /// Deserializes the value from bytes.
  T deserialize(byte[] bytes);
}
