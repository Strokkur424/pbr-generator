package net.strokkur.pbr;

import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

record PbrGenOptions(
    boolean enableInMemoryCache,
    @Nullable Path cacheDir,
    @Nullable ExecutorService executorService
) {
}
