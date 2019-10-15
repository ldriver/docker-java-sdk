package io.github.manuelkollus.docker.system;

import java.util.Objects;

public final class SystemException extends Exception {
  private SystemException(String message) {
    super(message);
  }

  public static SystemException withMessage(String message) {
    Objects.requireNonNull(message);
    return new SystemException(message);
  }
}
