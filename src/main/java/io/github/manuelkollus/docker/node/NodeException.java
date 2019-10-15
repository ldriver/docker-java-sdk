package io.github.manuelkollus.docker.node;

import java.util.Objects;

public final class NodeException extends Exception {
  private NodeException(String message) {
    super(message);
  }

  public static NodeException withMessage(String message) {
    Objects.requireNonNull(message);
    return new NodeException(message);
  }
}
