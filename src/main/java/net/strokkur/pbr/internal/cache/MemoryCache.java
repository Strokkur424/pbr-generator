package net.strokkur.pbr.internal.cache;

import net.strokkur.pbr.internal.serializer.CacheSerializable;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

final class MemoryCache<T extends CacheSerializable> implements PbrCache<T> {
  private final Map<CacheKey, T> cache = Collections.synchronizedMap(new TreeMap<>());

  @Override
  public T getOrSave(String key, long hash, Supplier<T> supplier) {
    return cache.computeIfAbsent(
        new CacheKey(key, hash),
        (ignored) -> supplier.get()
    );
  }

  @Override
  public void clear() {
    cache.clear();
  }

  private record CacheKey(
      String key,
      long hash
  ) implements Comparable<CacheKey> {
    @Override
    public int compareTo(CacheKey o) {
      final int keyCompare = key().compareTo(o.key());
      return keyCompare != 0 ? keyCompare : Long.compareUnsigned(hash(), o.hash());
    }
  }
}
