package io.github.manuelkollus.docker;

import java.util.Map;
import java.util.Objects;

public final class Messages {
  private String message;
  private Map<String, String> patterns;

  private Messages(
    String message,
    Map<String, String> patterns
  ) {
    this.message = message;
    this.patterns = patterns;
  }

  public String message() {
    for (Map.Entry<String, String> entry : patterns.entrySet()) {
      message = message.replace(entry.getKey(), entry.getValue());
    }
    return message;
  }

  public static Messages of(
    String value,
    Map<String, String> patterns
  ) {
    Objects.requireNonNull(value);
    Objects.requireNonNull(patterns);
    return new Messages(value, patterns);
  }
}
