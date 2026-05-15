package net.strokkur.pbr;

import net.strokkur.pbr.internal.cache.PbrCache;
import net.strokkur.pbr.internal.serializer.CacheSerializer;
import net.strokkur.pbr.map.NormalMap;
import net.strokkur.pbr.map.SpecularMap;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

final class PbrGenImpl implements PbrGen {
  private final PbrCache<NormalMap> normalCache;
  private final PbrCache<SpecularMap> specularCache;

  private final @Nullable PbrCache<NormalMap> inMemoryNormalCache;
  private final @Nullable PbrCache<SpecularMap> inMemorySpecularCache;

  private final @Nullable PbrCache<NormalMap> fileNormalCache;
  private final @Nullable PbrCache<SpecularMap> fileSpecularCache;

  PbrGenImpl(PbrGenOptions options) throws IOException {
    this.inMemoryNormalCache = options.enableInMemoryCache() ? PbrCache.createMemoryCache() : null;
    this.inMemorySpecularCache = options.enableInMemoryCache() ? PbrCache.createMemoryCache() : null;

    this.fileNormalCache = options.cacheDir() != null
        ? PbrCache.createFileCache(options.cacheDir().resolve("normal"), CacheSerializer.normal())
        : null;
    this.fileSpecularCache = options.cacheDir() != null
        ? PbrCache.createFileCache(options.cacheDir().resolve("specular"), CacheSerializer.specular())
        : null;

    this.normalCache = PbrCache.createDelegatingCache(Arrays.asList(inMemoryNormalCache, fileNormalCache));
    this.specularCache = PbrCache.createDelegatingCache(Arrays.asList(inMemorySpecularCache, fileSpecularCache));
  }

  @Override
  public NormalMap getNormal(TextureSource source) {
    return normalCache.getOrSave(source.key(), source.hashWithXXH3(), () ->
        NormalMap.fromTexture(source)
    );
  }

  @Override
  public NormalMap getNormal(TextureAtlasSource atlasSource) {
    return normalCache.getOrSave(atlasSource.key(), atlasSource.hashWithXXH3(), () ->
        NormalMap.fromAtlas(atlasSource)
    );
  }

  @Override
  public SpecularMap getSpecular(TextureSource source) {
    return specularCache.getOrSave(source.key(), source.hashWithXXH3(), () ->
        SpecularMap.fromTexture(source)
    );
  }

  @Override
  public SpecularMap getSpecular(TextureAtlasSource atlasSource) {
    return specularCache.getOrSave(atlasSource.key(), atlasSource.hashWithXXH3(), () ->
        SpecularMap.fromAtlas(atlasSource)
    );
  }

  @Override
  public void clearMemoryCache() {
    if (inMemoryNormalCache != null) {
      inMemoryNormalCache.clear();
    }
    if (inMemorySpecularCache != null) {
      inMemorySpecularCache.clear();
    }
  }

  @Override
  public void clearFileCache() {
    if (fileNormalCache != null) {
      fileNormalCache.clear();
    }
    if (fileSpecularCache != null) {
      fileSpecularCache.clear();
    }
  }
}
