package net.strokkur.pbr.internal.serializer;

import net.strokkur.pbr.map.SpecularMap;

import java.nio.ByteBuffer;
import java.util.Arrays;

final class SpecularMapSerializer implements CacheSerializer<SpecularMap> {
  static SpecularMapSerializer INSTANCE = new SpecularMapSerializer();

  @Override
  public byte[] serialize(SpecularMap value) {
    final ByteBuffer buffer = ByteBuffer.allocate(
      value.data().length + 2 * Integer.BYTES
    );

    buffer.putInt(value.width());
    buffer.putInt(value.height());
    buffer.put(value.data());

    return buffer.array();
  }

  @Override
  public SpecularMap deserialize(byte[] bytes) {
    final ByteBuffer buffer = ByteBuffer.wrap(bytes);

    final int width = buffer.getInt();
    final int height = buffer.getInt();
    final byte[] data = Arrays.copyOfRange(bytes, 2, bytes.length);

    return SpecularMap.direct(data, width, height);
  }
}
