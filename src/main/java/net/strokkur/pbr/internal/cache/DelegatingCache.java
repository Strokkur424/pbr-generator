package net.strokkur.pbr.internal.cache;

import net.strokkur.pbr.internal.serializer.CacheSerializable;

import java.util.function.Supplier;

class DelegatingCache<T extends CacheSerializable> implements PbrCache<T> {
  private final PbrCache<T>[] inner;

  public DelegatingCache(PbrCache<T>[] inner) {
    this.inner = inner;
  }

  @Override
  public T getOrSave(String key, long hash, Supplier<T> supplier) {
    return getFromCacheIndex(key, hash, supplier, 0);
  }

  /// Recursively fetches from caches, until no caches are left.
  private T getFromCacheIndex(String key, long hash, Supplier<T> supplier, int index) {
    if (index >= inner.length) {
      return supplier.get();
    }

    return inner[index].getOrSave(key, hash, () -> getFromCacheIndex(key, hash, supplier, index + 1));
  }

  @Override
  public void clear() {
    for (PbrCache<T> nested : inner) {
      nested.clear();
    }
  }
}
