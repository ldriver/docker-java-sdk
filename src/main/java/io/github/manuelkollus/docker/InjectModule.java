package io.github.manuelkollus.docker;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.github.manuelkollus.docker.node.NodePatternsFactory;
import io.github.manuelkollus.docker.swarm.SwarmPatternsFactory;
import io.github.manuelkollus.docker.system.SystemPatternsFactory;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;
import io.github.manuelkollus.docker.volume.VolumePatternsFactory;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public final class InjectModule extends AbstractModule {
  private DockerConfig config;

  private InjectModule(DockerConfig config) {
    this.config = config;
  }

  @Override
  protected void configure() {
    bind(DockerConfig.class).toInstance(config);
    configurePatterns();
  }

  private void configurePatterns() {
    PatternsFactory swarmPatterns = SwarmPatternsFactory.create();
    addPattern(swarmPatterns.createPatterns(), "Swarm");
    PatternsFactory systemPatterns = SystemPatternsFactory.create();
    addPattern(systemPatterns.createPatterns(), "System");
    PatternsFactory volumePatterns = VolumePatternsFactory.create();
    addPattern(volumePatterns.createPatterns(), "Volume");
    PatternsFactory nodePatterns = NodePatternsFactory.create();
    addPattern(nodePatterns.createPatterns(), "Node");
  }

  private void addPattern(Patterns patterns, String name) {
    bind(Patterns.class)
      .annotatedWith(Names.named(name))
      .toInstance(patterns);
  }

  private static final int GLOBAL_FALLBACK_EXECUTOR_SIZE = 2;

  @Provides
  @Singleton
  Executor createGlobalFallbackExecutor() {
    return Executors.newFixedThreadPool(GLOBAL_FALLBACK_EXECUTOR_SIZE);
  }

  @Provides
  @Singleton
  KeyPath createGlobalKeyPath() {
    return this.config.keyPath();
  }

  @Provides
  @Singleton
  public HttpClient createGlobalHttpClient() {
    return HttpClientBuilder.create()
      .build();
  }

  public static InjectModule of(DockerConfig config) {
    Objects.requireNonNull(config);
    return new InjectModule(config);
  }
}