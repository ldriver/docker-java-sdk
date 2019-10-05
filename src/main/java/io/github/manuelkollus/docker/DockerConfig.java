package io.github.manuelkollus.docker;

import java.util.Objects;

public final class DockerConfig {
  private KeyPath keyPath;

  private DockerConfig(KeyPath keyPath) {
    this.keyPath = keyPath;
  }

  public KeyPath keyPath() {
    return this.keyPath;
  }

  public static DockerConfig.Builder newBuilder() {
    return new DockerConfig.Builder();
  }

  public static final class Builder {
    private KeyPath keyPath;

    private Builder() {}

    public Builder withKeyPath(KeyPath keyPath) {
      this.keyPath = keyPath;
      return this;
    }

    public DockerConfig create() {
      Objects.requireNonNull(keyPath);
      return new DockerConfig(keyPath);
    }
  }
}