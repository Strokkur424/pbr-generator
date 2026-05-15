package net.strokkur.pbr.test;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.map.NormalMap;
import net.strokkur.pbr.map.SpecularMap;
import net.strokkur.pbr.texture.TextureSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    "/textures/bigger_img.png"
  };

  static void main() throws IOException {
    final PbrGen pbrGen = PbrGen.standard();

    final Path runFolder = Path.of("run");

    final Path normalFolder = runFolder.resolve("target/normal");
    final Path specularFolder = runFolder.resolve("target/specular");

    if (Files.notExists(normalFolder)) {
      Files.createDirectories(normalFolder);
    }
    if (Files.notExists(specularFolder)) {
      Files.createDirectories(specularFolder);
    }

    for (String texture : textures) {
      final URL url = TestApp.class.getResource(texture);
      if (url == null) {
        System.err.println("Failed to find resource: " + texture);
        continue;
      }

      final TextureSource source = TextureSource.load(texture.replace("/", ""), url);
      final NormalMap normalMap = pbrGen.getNormal(source);
      final SpecularMap specularMap = pbrGen.getSpecular(source);

      final BufferedImage normalImg = normalMap.toBufferedImage();
      final BufferedImage specularImg = specularMap.toBufferedImage();

      final String[] splitPath = texture.split("/");
      final String[] splitName = splitPath[splitPath.length - 1].split("\\.");

      ImageIO.write(normalImg, "png", normalFolder.resolve(splitName[0] + ".png").toFile());
      ImageIO.write(specularImg, "png", specularFolder.resolve(splitName[0] + ".png").toFile());
    }
  }
}
