package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import io.github.manuelkollus.docker.DockerConfig;
import io.github.manuelkollus.docker.HttpRequests;
import io.github.manuelkollus.docker.KeyPath;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.apache.http.client.HttpClient;

public final class SwarmRepository {
  private Executor executor;
  private HttpClient client;
  private DockerConfig dockerConfig;

  @Inject
  public SwarmRepository(
    Executor executor,
    HttpClient client,
    DockerConfig dockerConfig) {
    this.executor = executor;
    this.client = client;
    this.dockerConfig = dockerConfig;
  }

  public CompletableFuture<Swarm> inspectSwarm() {
    CompletableFuture<Swarm> future = new CompletableFuture<>();
    executor.execute(() -> inspectAndComplete(future));
    return future;
  }

  private void inspectAndComplete(CompletableFuture<Swarm> future) {
    KeyPath keyPath = dockerConfig.keyPath().subPath("swarm");
    String encodedString = HttpRequests.get(client, keyPath);
    Swarm swarm = inspectBlocking(encodedString);
    if (swarm == null) {
      String errorMessage = "Cannot find or parse to " + Swarm.class.getName();
      future.completeExceptionally(SwarmException.withMessage(errorMessage));
      return;
    }
    future.complete(swarm);
  }

  @Nullable
  private Swarm inspectBlocking(String encodedString) {
    return null;
  }
}
