package net.strokkur.pbr.jmh;

import net.strokkur.pbr.PbrGen;
import net.strokkur.pbr.texture.TextureSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class SpecularMapGenBenchmark {
  @Benchmark
  public void benchmarkCrafterNorth(LoadedResources res) {
    res.pbrGen.getSpecular(res.textureCrafterNorth);
  }

  @State(Scope.Benchmark)
  public static class LoadedResources {
    private final PbrGen pbrGen = PbrGen.standard();
    private final TextureSource textureCrafterNorth;

    {
      try {
        textureCrafterNorth = TextureSource.load(
          "crafter_north",
          Objects.requireNonNull(LoadedResources.class.getResource("/crafter_north.png"))
        );
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
