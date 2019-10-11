package io.github.manuelkollus.docker;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.googlecode.protobuf.format.JsonFormat;
import io.github.manuelkollus.docker.swarm.SwarmPatternsFactory;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public final class InjectModule extends AbstractModule {
  private JsonFormat format;
  private DockerConfig config;

  private InjectModule(DockerConfig config) {
    this.config = config;
    this.format = new JsonFormat();
  }

  @Override
  protected void configure() {
    bind(DockerConfig.class).toInstance(config);
    bind(PatternsFactory.class)
      .annotatedWith(Names.named("Swarm"))
      .toInstance(SwarmPatternsFactory.create());
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
      .setDefaultCredentialsProvider(authHttpClient())
      .build();
  }

  private CredentialsProvider authHttpClient() {
    CredentialsProvider provider = new BasicCredentialsProvider();
    provider.setCredentials(AuthScope.ANY, config.credentials());
    return provider;
  }

  public static InjectModule of(DockerConfig config) {
    Objects.requireNonNull(config);
    return new InjectModule(config);
  }
}