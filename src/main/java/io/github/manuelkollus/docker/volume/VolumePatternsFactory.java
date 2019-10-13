package io.github.manuelkollus.docker.volume;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class VolumePatternsFactory implements PatternsFactory {
  private VolumePatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    //TODO: add the volume patterns
    return Patterns.newBuilder()
      .create();
  }

  public static PatternsFactory create() {
    return new VolumePatternsFactory();
  }
}
