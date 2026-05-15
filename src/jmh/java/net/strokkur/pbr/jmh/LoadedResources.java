package net.strokkur.pbr.jmh;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.texture.TextureSource;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Objects;

@State(Scope.Benchmark)
public class LoadedResources {
  final PbrGen pbrGen = PbrGen.standard();
  final TextureSource textureCrafterNorth;
  final TextureSource textureBiggerImage;

  {
    try {
      textureCrafterNorth = TextureSource.load(
        "crafter_north",
        Objects.requireNonNull(LoadedResources.class.getResource("/crafter_north.png"))
      );
      textureBiggerImage = TextureSource.load(
        "bigger_img",
        Objects.requireNonNull(LoadedResources.class.getResource("/bigger_img.png"))
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
