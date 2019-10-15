package io.github.manuelkollus.docker.swarm;

import java.util.Objects;

public final class SwarmException extends Exception {
  private SwarmException(String message) {
    super(message);
  }

  public static SwarmException withMessage(String message) {
    Objects.requireNonNull(message);
    return new SwarmException(message);
  }
}
