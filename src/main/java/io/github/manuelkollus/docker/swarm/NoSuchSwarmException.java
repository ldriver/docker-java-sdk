package io.github.manuelkollus.docker.swarm;

import java.util.Objects;

public final class NoSuchSwarmException extends Exception {
  private NoSuchSwarmException(String message) {
    super(message);
  }

  public static NoSuchSwarmException withMessage(String message) {
    Objects.requireNonNull(message);
    return new NoSuchSwarmException(message);
  }
}
