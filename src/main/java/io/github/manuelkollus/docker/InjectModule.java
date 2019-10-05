package io.github.manuelkollus.docker;

import com.google.inject.AbstractModule;
import java.util.Objects;

public final class InjectModule extends AbstractModule {
  private DockerConfig config;

  private InjectModule(DockerConfig config) {
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(DockerConfig.class).toInstance(config);
  }

  public static InjectModule of(DockerConfig config) {
    Objects.requireNonNull(config);
    return new InjectModule(config);
  }
}