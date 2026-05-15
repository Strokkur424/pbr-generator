package net.strokkur.pbr.internal.serializer;

import net.strokkur.pbr.map.NormalMap;

import java.nio.ByteBuffer;

final class NormalMapSerializer implements CacheSerializer<NormalMap> {
  static NormalMapSerializer INSTANCE = new NormalMapSerializer();

  @Override
  public byte[] serialize(NormalMap value) {
    final ByteBuffer buffer = ByteBuffer.allocate(
      (2 + value.rgbaData().length) * Integer.BYTES
    );

    buffer.putInt(value.width());
    buffer.putInt(value.height());

    for (int rgba : value.rgbaData()) {
      buffer.putInt(rgba);
    }

    return buffer.array();
  }

  @Override
  public NormalMap deserialize(byte[] bytes) {
    final ByteBuffer buffer = ByteBuffer.wrap(bytes);

    final int width = buffer.getInt();
    final int height = buffer.getInt();
    final int[] rgbaData = new int[width * height];

    int index = 0;
    while (buffer.hasRemaining()) {
      rgbaData[index++] = buffer.getInt();
    }

    return NormalMap.direct(rgbaData, width, height);
  }
}
