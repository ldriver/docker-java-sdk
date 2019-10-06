package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import com.google.protobuf.ExtensionRegistry;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import io.github.manuelkollus.docker.DockerConfig;
import io.github.manuelkollus.docker.HttpRequests;
import io.github.manuelkollus.docker.KeyPath;
import io.github.manuelkollus.docker.Message;
import io.github.manuelkollus.docker.Messages;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.apache.http.client.HttpClient;

public final class SwarmRepository {
  private Executor executor;
  private JsonFormat format;
  private HttpClient client;
  private DockerConfig dockerConfig;

  @Inject
  private SwarmRepository(
    Executor executor,
    HttpClient client,
    DockerConfig dockerConfig
  ) {
    this.executor = executor;
    this.format = new JsonFormat();
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
    String encodeString = HttpRequests.get(client, keyPath);
    Swarm swarm = inspectBlocking(encodeString);
    if (swarm == null) {
      String errorMessage = "Cannot find or parse to " + Swarm.class.getName();
      future.completeExceptionally(NoSuchSwarmException.withMessage(errorMessage));
      return;
    }
    future.complete(swarm);
  }

  @Nullable
  private Swarm inspectBlocking(String encodeString) {
    Message message = Messages.of(encodeString, SwarmReplacePattern.patterns());
    Swarm.Builder builder = Swarm.newBuilder();
    try {
      format.merge(
        message.message(),
        ExtensionRegistry.getEmptyRegistry(),
        builder);
    } catch (ParseException swarmParseFailure) {
      swarmParseFailure.printStackTrace();
    }
    return builder.build();
  }
}
