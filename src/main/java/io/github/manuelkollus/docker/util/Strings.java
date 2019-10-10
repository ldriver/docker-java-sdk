package io.github.manuelkollus.docker.util;

public final class Strings {
  private Strings() {}

  public static String capitalize(String value) {
    if (isNullOrEmpty(value)) {
      return "";
    }
    return value.substring(0, 1).toUpperCase() + value.substring(1);
  }

  private static boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }
}