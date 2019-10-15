package io.github.manuelkollus.docker.volume;

import java.util.Objects;

public final class VolumeException extends Exception {
  private VolumeException(String message) {
    super(message);
  }

  public static VolumeException withMessage(String message) {
    Objects.requireNonNull(message);
    return new VolumeException(message);
  }
}
