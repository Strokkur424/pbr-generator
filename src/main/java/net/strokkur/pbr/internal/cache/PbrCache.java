package net.strokkur.pbr.internal.cache;

import net.strokkur.pbr.internal.serializer.CacheSerializable;
import net.strokkur.pbr.internal.serializer.CacheSerializer;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

/// A cache avoids repetitive calculations by first attempting to fetch precomputed results
/// from external sources before attempting to actually compute the result anew.
public interface PbrCache<T extends CacheSerializable> {

  /// Creates a new file-system backed cache.
  static <T extends CacheSerializable> PbrCache<T> createFileCache(Path path, CacheSerializer<T> serializer)
      throws IOException {
    return new FileCache<>(path, serializer);
  }

  /// Creates a new in-memory cache.
  static <T extends CacheSerializable> PbrCache<T> createMemoryCache() {
    return new MemoryCache<>();
  }

  /// Creates a special type of cache that attempts to fetch a cache entry from each of the
  /// provided ones before running the supplier. The caches provided as parameters to this
  /// method should be sorted by longevity, meaning the less-persistent, but faster caches
  /// should be provided first. If no caches are provided, the returned "cache" will always
  /// run the value supplied without any sort of caching.
  ///
  /// The map automatically filters by nullability. This is provided as a convenience for inline
  /// list creation.
  static <T extends CacheSerializable> PbrCache<T> createDelegatingCache(List<@Nullable PbrCache<T>> nested) {
    //noinspection unchecked
    return new DelegatingCache<>(nested.toArray(PbrCache[]::new));
  }

  /// Gets a stored value from the cache with the provided key-hash-pair.
  /// If no value is present, it will fetch the value from the provided supplier,
  /// save it to the cache, and only then return the value.
  T getOrSave(String key, long hash, Supplier<T> supplier);

  /// Clears this cache. **This action is irreversible**.
  void clear();
}
