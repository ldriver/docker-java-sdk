package io.github.manuelkollus.docker.system;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class SystemPatternsFactory implements PatternsFactory {
  private SystemPatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    return Patterns.newBuilder()
      .create();
  }

  public static PatternsFactory create() {
    return new SystemPatternsFactory();
  }
}
