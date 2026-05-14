package net.strokkur.pbr.texture;

import java.util.Map;
import java.util.function.BiFunction;

public record Pixel(
    float alpha,
    float red,
    float green,
    float blue
) {
  public Pixel(int argb) {
    this(
        (argb >> 8 * 3 & 0xFF) / 255f,
        (argb >> 8 * 2 & 0xFF) / 255f,
        (argb >> 8 & 0xFF) / 255f,
        (argb & 0xFF) / 255f
    );
  }

  /// Computes the luminance from the RGB data of this pixel.
  ///
  /// Based on [this article](https://www.w3.org/TR/AERT/#color-contrast).
  public float luminance() {
    return 0.299f * red + 0.587f * green + 0.114f * blue;
  }

  /// Computes the hue from the RGB data of this pixel.
  ///
  /// Based on [this article](https://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/).
  public float hue() {
    final Map<Float, BiFunction<Float, Float, Float>> hueFunctions = Map.of(
        red, (max, min) -> (green - blue) / (max - min),
        green, (max, min) -> 2.0f + (blue - red) / (max - min),
        blue, (max, min) -> 4.0f + (red - green) / (max - min));
    return hueFunctions.entrySet().stream()
        .min(Map.Entry.comparingByKey())
        .orElseThrow()
        .getValue().apply(
            hueFunctions.keySet().stream().max(Float::compareTo).orElseThrow(),
            hueFunctions.keySet().stream().min(Float::compareTo).orElseThrow()
        );
  }
}
