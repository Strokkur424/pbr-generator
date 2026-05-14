package net.strokkur.pbr.internal.cache;

import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.internal.serializer.CacheSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

class FileCache<T extends CacheSerializable> implements PbrCache<T> {
  private final Path path;
  private final CacheSerializer<T> serializer;

  private final ReadWriteLock cacheClearLock = new ReentrantReadWriteLock();

  FileCache(Path path, CacheSerializer<T> serializer) throws IOException {
    this.path = path;
    this.serializer = serializer;
    Files.createDirectories(path);
  }

  /// This is *technically* not actually thread safe. If two threads were to call this method concurrently with the same
  /// key and hash, this might turn into a race condition. However, this scenario is so ridiculously rare that nobody
  /// should realistically run into that problem, unless doing some very stupid things.
  @Override
  public T getOrSave(String key, long hash, Supplier<T> supplier) {
    final Lock lock = cacheClearLock.readLock();
    try {
      lock.lock();
      final Path filePath = path.resolve("%s-%s.bin".formatted(key, Long.toHexString(hash)));
      if (Files.exists(filePath)) {
        final byte[] bytes = Files.readAllBytes(filePath);
        return serializer.deserialize(bytes);
      }

      final T value = supplier.get();
      Files.write(filePath, serializer.serialize(value));
      return value;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void clear() {
    final Lock lock = cacheClearLock.writeLock();
    try {
      lock.lock();
      try (Stream<Path> stream = Files.list(path)) {
        for (Path path : stream.toList()) {
          Files.delete(path);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }
}
