package io.github.manuelkollus.docker.util.protobuf;

public final class Pattern {
  private String key;
  private String value;

  private Pattern(String key, String value) {
    this.key = key;
    this.value = value;
  }

  String replace(String text, PatternReplacementStrategy strategy) {
    switch (strategy) {
      case KEY:
        text = text.replace(key, value);
        break;
      case VALUE:
        text = text.replace(value, key);
        break;
    }
    return text;
  }

  public static Pattern create(String key, String value) {
    return new Pattern("\"" + key + "\"", "\"" + value + "\"");
  }
}