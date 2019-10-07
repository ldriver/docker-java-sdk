package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import com.google.protobuf.ExtensionRegistry;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import io.github.manuelkollus.docker.HttpRequests;
import io.github.manuelkollus.docker.KeyPath;
import io.github.manuelkollus.docker.Message;
import io.github.manuelkollus.docker.Messages;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.apache.http.client.HttpClient;

public final class SwarmRepository {
  private KeyPath path;
  private Executor executor;
  private JsonFormat format;
  private HttpClient client;

  @Inject
  private SwarmRepository(
    KeyPath path,
    Executor executor,
    JsonFormat format,
    HttpClient client
  ) {
    this.path = path;
    this.executor = executor;
    this.format = format;
    this.client = client;
  }

  public CompletableFuture<String> initializeSwarm(SwarmInit swarmInit) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executor.execute(() -> initializeAndComplete(swarmInit, future));
    return future;
  }

  private void initializeAndComplete(
    SwarmInit swarmInit,
    CompletableFuture<String> future
  ) {
    String encodeString = format.printToString(swarmInit);
    Message message = Messages.of(encodeString, SwarmReplacePattern.patterns());
    String blockingResponse = initializeBlocking(message.message());
    if (blockingResponse == null) {
      String errorMessage = "Cannot initialize the Swarm";
      future.completeExceptionally(
        SwarmInitializationException.withMessage(errorMessage));
      return;
    }
    future.complete(blockingResponse);
  }

  private String initializeBlocking(String encodeString) {
    KeyPath initializePath = path.subPath("init");
    return HttpRequests.post(
      client, initializePath, encodeString
    );
  }

  public CompletableFuture<Swarm> inspectSwarm() {
    CompletableFuture<Swarm> future = new CompletableFuture<>();
    executor.execute(() -> inspectAndComplete(future));
    return future;
  }

  private void inspectAndComplete(CompletableFuture<Swarm> future) {
    String encodeString = HttpRequests.get(client, path);
    Swarm swarm = inspectBlocking(encodeString);
    if (swarm == null) {
      String errorMessage = "Cannot find or parse to " + Swarm.class.getName();
      future.completeExceptionally(
        NoSuchSwarmException.withMessage(errorMessage));
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