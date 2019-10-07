package io.github.manuelkollus.docker.swarm;

import java.util.Objects;

public final class SwarmInitializationException extends Exception {
  public SwarmInitializationException(String message) {
    super(message);
  }

  public static SwarmInitializationException withMessage(String message) {
    Objects.requireNonNull(message);
    return new SwarmInitializationException(message);
  }
}