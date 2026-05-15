package net.strokkur.pbr.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

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

  @Benchmark
  public void benchmarkBiggerImg(LoadedResources res) {
    res.pbrGen.getSpecular(res.textureBiggerImage);
  }
}
