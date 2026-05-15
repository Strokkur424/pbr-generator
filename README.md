# pbr-generator

This is a simple library with the purpose of generating PBR (Physically Based Rendering) data from textures,
with a focus on Minecraft assets.

## Adding the dependency

Add the following to your `build.gradle.kts`:
```kts
repositories {
  maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
  // Replace <version> with the latest version. See the GitHub Releases tab.
  implementation("net.strokkur:pbr-generator:<version>")
}
```

## Currently supported (and planned) generators:

- [x] Normal
- [x] Specular

## Library usage

At its heart stands the `PbrGen` interface. You can get an instance using the `PbrGenBuilder`:
```java
public static final PbrGen PBR_GEN = PbrGen.builder()
    // If you want to configure PBR caching to disk.
    .withCacheDir(path)

    // If you want to configure in-memory PBR caching, useful when
    // expecting frequent resource reloads.
    .withInMemoryCache()

    // Configures parallel-processing when processing 
    // entire atlas textures at a time.
    .withMultiThreading(executorService)

    // Creates a new PbrGen instance. The cache is instance-dependent, so you should
    // always keep the same instance around and not recreate it each time.
    .build();
```

The `PbrGen` interface has **very** extensive Javadoc documentation. You should give it a read if you want
to understand the internal processes more. The Javadocs are not hosted anywhere, however Javadocs/sources JARs
are published alongside the normal dependency artifact, so you should be able to view the Javadocs in your IDE.

General usage looks like this (example: normal map generation):
```java
// Get a texture source.
final TextureSource source = TextureSource.load("stone", assetDir.resolve("stone.png"));

// Pass the source to PbrGen#getNormal. This first tries to load from the cache,
// generating the normal map only if it fails.
final NormalMap normal = PBR_GEN.getNormal(source);

// You can now get the RGBA pixel data for the generated normal map and
// continue processing these (write to file or upload to GPU).
final int[] normalPixels = normal.rgbaData();
```

## License
This project is licensed under the terms of the MIT license. For more details, see [LICENSE.md](LICENSE.md).
