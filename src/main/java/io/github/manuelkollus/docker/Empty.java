package io.github.manuelkollus.docker;

public final class Empty {
  private Empty() {}

  public static Empty create() {
    return new Empty();
  }
}