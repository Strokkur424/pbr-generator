package net.strokkur.pbr;

import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

final class PbrGenBuilderImpl implements PbrGenBuilder {
  private boolean enableInMemoryCache = false;
  private @Nullable Path cacheDir = null;
  private @Nullable ExecutorService executorService;

  @Override
  public PbrGenBuilder withCacheDir(Path directory) {
    this.cacheDir = directory;
    return this;
  }

  @Override
  public PbrGenBuilder withInMemoryCache() {
    this.enableInMemoryCache = true;
    return this;
  }

  @Override
  public PbrGenBuilder withMultiThreading(ExecutorService service) {
    this.executorService = service;
    return this;
  }

  @Override
  public PbrGen build() {
    try {
      return new PbrGenImpl(new PbrGenOptions(
          enableInMemoryCache,
          cacheDir,
          executorService
      ));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
