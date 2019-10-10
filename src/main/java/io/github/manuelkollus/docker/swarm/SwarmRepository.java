package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.StringEncodings;
import io.github.manuelkollus.docker.util.http.HttpClients;
import io.github.manuelkollus.docker.util.http.Response;
import io.github.manuelkollus.docker.util.protobuf.MessageReader;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public final class SwarmRepository {
  private KeyPath path;
  private Executor executor;
  private Patterns patterns;
  private HttpClients client;
  private MessageReader reader;

  @Inject
  private SwarmRepository(
    KeyPath path,
    Executor executor,
    HttpClients client,
    MessageReader reader,
    @Named("Swarm") PatternsFactory factory
  ) {
    this.path = path.subPath("swarm");
    this.executor = executor;
    this.patterns = factory.createPatterns();
    this.client = client;
    this.reader = reader;
  }

  public CompletableFuture<String> initializeSwarm(SwarmInitRequest request) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executor.execute(() -> initializeAndComplete(request, future));
    return future;
  }

  private void initializeAndComplete(
    SwarmInitRequest request, CompletableFuture<String> future) {
    Response response = initializeBlocking(request);
    checkInitialRequestStatusCode(response, future);
    String content = StringEncodings.encodeUtf8(response.content());
    future.complete(content);
  }

  private Response initializeBlocking(SwarmInitRequest request) {
    KeyPath path = this.path.subPath("init");
    return client.post(
      path,
      request,
      patterns
    );
  }

  private void checkInitialRequestStatusCode(
    Response response, CompletableFuture<String> future) {
    if (isRequestFailed(response.code())) {
      String errorMessage = "The initial request could not be executed the"
        + " status code is " + response.code();
      future.completeExceptionally(SwarmException.withMessage(errorMessage));
    }
  }

  public CompletableFuture<Swarm> inspectSwarm() {
    CompletableFuture<Swarm> future = new CompletableFuture<>();
    executor.execute(() -> inspectAndComplete(future));
    return future;
  }

  private void inspectAndComplete(CompletableFuture<Swarm> future) {
    Swarm swarm = inspectBlocking();
    future.complete(swarm);
  }

  @Nullable
  private Swarm inspectBlocking() {
    Response response = client.get(path);
    Swarm.Builder builder = Swarm.newBuilder();
    reader.readMessage(
      response.content(),
      builder,
      patterns
    );
    return builder.build();
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