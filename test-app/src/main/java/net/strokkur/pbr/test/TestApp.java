package net.strokkur.pbr.test;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.texture.TextureSource;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestApp {
  private static final String[] textures = {
    "/textures/amethyst_block.png",
    "/textures/blast_furnace_front.png",
    "/textures/cartography_table_side3.png",
    "/textures/comparator.png",
    "/textures/copper_block.png",
    "/textures/crafter_north.png",
    "/textures/obsidian.png",
    "/textures/piston_side.png",
    "/textures/respawn_anchor_side4.png",
  };

  static void main() throws IOException {
    final PbrGen pbrGen = PbrGen.standard();

    final Path runFolder = Path.of("run");
    final Path targetFolder = runFolder.resolve("target");

    if (Files.notExists(targetFolder)) {
      Files.createDirectories(targetFolder);
    }

    for (String texture : textures) {
      final URL url = TestApp.class.getResource(texture);
      if (url == null) {
        System.err.println("Failed to find resource: " + texture);
        continue;
      }

      final TextureSource source = TextureSource.load(texture, url);
      // Continuation follows...
    }
  }
}
