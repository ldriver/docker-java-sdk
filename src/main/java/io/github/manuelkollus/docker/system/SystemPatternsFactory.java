package io.github.manuelkollus.docker.system;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class SystemPatternsFactory implements PatternsFactory {
  private SystemPatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    Patterns.Builder builder = Patterns.newBuilder();
    addVersionPatterns(builder);
    return builder.create();
  }

  private void addVersionPatterns(Patterns.Builder builder) {
    builder.addPattern("version")
      .addPattern("os")
      .addPattern("kernelVersion")
      .addPattern("goVersion")
      .addPattern("gitCommit")
      .addPattern("arch")
      .addPattern("apiVersion")
      .addPattern("minApiVersion", "MinAPIVersion")
      .addPattern("buildTime")
      .addPattern("experimental");
  }

  public static PatternsFactory create() {
    return new SystemPatternsFactory();
  }
}
