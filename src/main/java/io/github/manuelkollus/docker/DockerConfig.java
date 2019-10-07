package io.github.manuelkollus.docker;

import java.util.Objects;
import org.apache.http.auth.UsernamePasswordCredentials;

public final class DockerConfig {
  private KeyPath keyPath;
  private UsernamePasswordCredentials credentials;

  private DockerConfig() {}

  private DockerConfig(
    KeyPath keyPath,
    UsernamePasswordCredentials credentials
  ) {
    this.keyPath = keyPath;
    this.credentials = credentials;
  }

  public KeyPath keyPath() {
    return this.keyPath;
  }

  public UsernamePasswordCredentials credentials() {
    return this.credentials;
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

    public Builder withCredentials(UsernamePasswordCredentials credentials) {
      Objects.requireNonNull(credentials);
      this.prototype.credentials = credentials;
      return this;
    }

    public DockerConfig create() {
      return new DockerConfig(
        prototype.keyPath,
        prototype.credentials
      );
    }
  }
}