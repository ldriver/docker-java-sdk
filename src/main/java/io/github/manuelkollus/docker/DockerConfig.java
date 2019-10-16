package io.github.manuelkollus.docker;

import io.github.manuelkollus.docker.util.DelimitedKeyPath;
import io.github.manuelkollus.docker.util.KeyPath;
import java.util.Objects;

public final class DockerConfig {
  private KeyPath keyPath;
  private AuthenticationConfig authentication;

  private DockerConfig() {}

  private DockerConfig(
    KeyPath keyPath, AuthenticationConfig authentication) {
    this.keyPath = keyPath;
    this.authentication = authentication;
  }

  public KeyPath keyPath() {
    return this.keyPath;
  }

  public AuthenticationConfig authentication() {
    return this.authentication;
  }

  public static DockerConfig.Builder newBuilder() {
    return new DockerConfig.Builder(new DockerConfig());
  }

  public static final class Builder {
    private DockerConfig prototype;

    private Builder(DockerConfig prototype) {
      this.prototype = prototype;
    }

    public Builder withKeyPath(String keyPath) {
      Objects.requireNonNull(keyPath);
      this.prototype.keyPath = DelimitedKeyPath.create("/", keyPath);
      return this;
    }

    public Builder withAuthentication(AuthenticationConfig authentication) {
      Objects.requireNonNull(authentication);
      this.prototype.authentication = authentication;
      return this;
    }

    public DockerConfig create() {
      return new DockerConfig(
        prototype.keyPath,
        prototype.authentication
      );
    }
  }
}