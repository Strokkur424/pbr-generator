package net.strokkur.pbr;

import net.openhft.hashing.LongHashFunction;
import net.strokkur.pbr.map.NormalMap;
import net.strokkur.pbr.map.SpecularMap;
import net.strokkur.pbr.texture.TextureAtlasSource;
import net.strokkur.pbr.texture.TextureSource;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/// Holds all methods relevant when interfacing with Strokkur's pbr-generator library.
///
/// [TextureSource] and [TextureAtlasSource] instances can be obtained through their respective `load` methods
/// on the interfaces themselves. Supported sources are [File]/[Path] and [URI]/[URL].
///
/// ## Thread safety
/// All instance methods here are thread-safe. Most objects used internally are **immutable**, requiring
/// no copying or external synchronization. The few times when mutability is required, proper synchronization
/// (typically through the use of a [ReentrantReadWriteLock]) is ensured.
///
/// Image manipulation is generally a performance costly task; however, the cost that thread context-switching
/// brings with it oftentimes does not warrant parallel processing.
///
/// *However*, with large image counts grouping multiple textures together into one thread, which allows for multiple
/// threads to work in parallel can outperform a single-threaded solution rather quickly. If multi threading was
/// configured using the [#builder()], using [#getNormal(TextureAtlasSource)] may already do the relevant
/// grouping and multithreading by itself; hence this method is recommended over manual grouping.
///
/// The methods provided here do not return a [Future], instead they are blocking. If you want to run other code
/// whilst the textures are being processed, you may wrap the method call into a [CompletableFuture] and
/// [join][CompletableFuture#join()] it whenever you require the result.
///
/// ## Specification
/// When provided with a texture, `pbr-generator` can generate the following PBR properties:
///
/// ### Normal maps
/// For more details, see [NormalMap].
///
/// ### Specular maps
/// For more details, see [SpecularMap].
///
/// ## Caching
/// This library supports caching. There are two types of caches, both of which are implemented using a combined value
/// from [TextureSource#key()] or [TextureAtlasSource#key()] and the [XXH3](https://github.com/cyan4973/xxhash)
/// hashing algorithm, which is known for its performance. The Java bindings are provided by
/// [Zero-Allocation-Hashing](https://github.com/OpenHFT/Zero-Allocation-Hashing) through [LongHashFunction#xx3()].
///
/// Caches apply to both [TextureSource] and [TextureAtlasSource] objects. Passing a [TextureAtlasSource]
/// will result in only one hash value pointing to the full resulting texture atlas. Individual
/// sprites are not cached separately.
///
/// ### In-memory caching
/// If enabled ([PbrGenBuilder#withInMemoryCache()]), operation results are hashed and cached in-memory.
/// This cache will live as long as the program is online for, making it ideal if frequent resource reloads
/// are expected. The in-memory cache can be cleared using [#clearMemoryCache()], which will free all memory
/// used by it.
///
/// **In memory caches are per-instance, meaning that creating two [PbrGen] instances will result in two separate
/// caches being kept track of, which might not be what you want!**
///
/// The original texture is not kept in memory; only the texture hash (8 bytes) and string key, together
/// with the result, are stored.
///
/// ### File-system caching
/// If a cache directory ([PbrGenBuilder#withCacheDir(Path)]) was set, the result of an operation will be
/// stored on disk. The folder structure for caches looks something like this:
/// ```
/// <cache-dir>/
/// ├── normal/
/// │   ├── <key>-<hash>.bin
/// │   └── ...
/// └── specular/
///     ├── <key>-<hash>.bin
///     └── ...
/// ```
/// The cached values are uncompressed, formatless binary files, optimized for quick saving and loading.
public sealed interface PbrGen permits PbrGenImpl {

  /// Creates a standard [PbrGen] instance with the default options. This instance
  /// does not do any caching and does not do any parallel processing for [TextureAtlasSource]
  /// objects.
  static PbrGen standard() {
    return builder().build();
  }

  /// Creates a new [PbrGenBuilder], which you can use to explicitly enable optional features.
  static PbrGenBuilder builder() {
    return new PbrGenBuilderImpl();
  }

  /// If the result of this operation was already cached for this [TextureSource], this
  /// method simply retrieves it from the cache. If not present, the normal map will first
  /// be generated, stored in any configured cache, and finally returned.
  ///
  /// @param source the texture source to fetch the normal map for
  /// @return the normal map for this texture
  NormalMap getNormal(TextureSource source);

  /// If the result of this operation was already cached for this [TextureAtlasSource], this
  /// method simply retrieves it from the cache. If not present, the normal map will first
  /// be generated, stored in any configured cache, and finally returned.
  ///
  /// @param atlasSource the atlas texture source to fetch the normal map for
  /// @return the normal map for this atlas
  NormalMap getNormal(TextureAtlasSource atlasSource);

  /// If the result of this operation was already cached for this [TextureSource], this
  /// method simply retrieves it from the cache. If not present, the specular map will first
  /// be generated, stored in any configured cache, and finally returned.
  ///
  /// @param source the texture source to fetch the normal map for
  /// @return the specular map for this texture
  SpecularMap getSpecular(TextureSource source);

  /// If the result of this operation was already cached for this [TextureAtlasSource], this
  /// method simply retrieves it from the cache. If not present, the specular map will first
  /// be generated, stored in any configured cache, and finally returned.
  ///
  /// @param atlasSource the atlas texture source to fetch the normal map for
  /// @return the specular map for this atlas
  SpecularMap getSpecular(TextureAtlasSource atlasSource);

  /// Clears the in-memory cache.
  void clearMemoryCache();

  /// Completely delete the cache directory and any files inside it. **This operation cannot be
  /// undone**.
  void clearFileCache();
}
