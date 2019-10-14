package io.github.manuelkollus.docker.node;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class NodePatternsFactory implements PatternsFactory {
  private NodePatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    return Patterns.newBuilder()
      .create();
  }

  public static PatternsFactory create() {
    return new NodePatternsFactory();
  }
}
