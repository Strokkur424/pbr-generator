package net.strokkur.pbr;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

public sealed interface PbrGenBuilder permits PbrGenBuilderImpl {

  /// Sets the cache directory, from where mapped textures will be saved to
  /// and possibly loaded from.
  PbrGenBuilder withCacheDir(Path directory);

  /// Enables the in-memory cache, which keeps generated texture data
  /// in-memory for as long as the program runs.
  PbrGenBuilder withInMemoryCache();

  /// Enables multi-threading processing for texture atlases. This
  /// uses the provided [ExecutorService] for task dispatching.
  PbrGenBuilder withMultiThreading(ExecutorService service);

  /// Creates a new [PbrGen] instance.
  PbrGen build();
}
