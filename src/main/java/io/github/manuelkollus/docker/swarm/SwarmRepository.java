package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import io.github.manuelkollus.docker.HttpRequests;
import io.github.manuelkollus.docker.HttpRequests.Response;
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
    this.path = path.subPath("swarm");
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
    String encodeString = formatGeneratedMessageToJson(swarmInit);
    Response response = initializeBlocking(encodeString);
    validateInitialRequestStatusCode(response, future);
    validateInitialBlockingResponseConsistency(response.content(), future);
  }

  private Response initializeBlocking(String encodeString) {
    KeyPath initializePath = path.subPath("init");
    return HttpRequests.post(
      client, initializePath, encodeString);
  }

  private void validateInitialBlockingResponseConsistency(
    String blockingResponse,
    CompletableFuture<String> future) {
    if (blockingResponse == null) {
      String errorMessage = "Cannot initialize the Swarm";
      future.completeExceptionally(
        SwarmInitializationException.withMessage(errorMessage));
      return;
    }
    future.complete(blockingResponse);
  }

  private void validateInitialRequestStatusCode(
    Response response,
    CompletableFuture<String> future
  ) {
    if (isRequestFailed(response.code())) {
      String errorMessage = "The initial request could not be executed the"
        + " status code is {0}";
      future.completeExceptionally(SwarmInitializationException
        .withMessage(String.format(errorMessage, response.code())));
    }
  }

  public CompletableFuture<Swarm> inspectSwarm() {
    CompletableFuture<Swarm> future = new CompletableFuture<>();
    executor.execute(() -> inspectAndComplete(future));
    return future;
  }

  private void inspectAndComplete(CompletableFuture<Swarm> future) {
    Response response = HttpRequests.get(client, path);
    Swarm swarm = inspectBlocking(response.content());
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

  private String formatGeneratedMessageToJson(
    GeneratedMessage generatedMessage) {
    String encodeString = format.printToString(generatedMessage);
    Message message = Messages.of(encodeString, SwarmReplacePattern.patterns());
    return message.message();
  }

  private static final int BAD_PARAMETER = 400;
  private static final int NO_SUCH_SWARM = 404;
  private static final int SERVER_ERROR = 500;
  private static final int NODE_IS_ALREADY_PART_OF_SWARM = 503;

  private boolean isRequestFailed(int code) {
    return code == BAD_PARAMETER ||
      code == NO_SUCH_SWARM ||
      code == SERVER_ERROR ||
      code == NODE_IS_ALREADY_PART_OF_SWARM;
  }
}