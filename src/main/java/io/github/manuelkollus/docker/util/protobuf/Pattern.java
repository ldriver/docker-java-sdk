package io.github.manuelkollus.docker.util.protobuf;

public final class Pattern {
  private String key;
  private String value;

  private Pattern(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String key() {
    return key;
  }

  public String replace(String text) {
    text = text.replace(key, value);
    return text;
  }


  public static Pattern create(String key, String value) {
    return new Pattern("\"" + key + "\"", "\"" + value + "\"");
  }
}
