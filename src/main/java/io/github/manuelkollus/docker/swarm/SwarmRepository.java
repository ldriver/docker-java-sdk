package io.github.manuelkollus.docker.swarm;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.StringEncodings;
import io.github.manuelkollus.docker.util.http.HttpClients;
import io.github.manuelkollus.docker.util.http.Response;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public final class SwarmRepository {

  private KeyPath path;
  private Executor executor;
  private Patterns patterns;
  private HttpClients client;

  @Inject
  private SwarmRepository(
    KeyPath path,
    Executor executor,
    HttpClients client,
    @Named("Swarm") Patterns patterns
  ) {
    this.path = path.subPath("swarm");
    this.executor = executor;
    this.patterns = patterns;
    this.client = client;
  }

  public CompletableFuture<String> initializeSwarm(SwarmInitRequest request) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executor.execute(() -> initializeAndComplete(request, future));
    return future;
  }

  private void initializeAndComplete(
    SwarmInitRequest request, CompletableFuture<String> future) {
    String content = initializeBlocking(request);
    if (content == null) {
      String errorMessage = "The swarm could not be initialized from the"
        + " servers, please check if the name of the swarm exists or if there"
        + "are any server errors.";
      future.completeExceptionally(SwarmException.withMessage(errorMessage));
      return;
    }
    future.complete(content);
  }

  @Nullable
  private String initializeBlocking(SwarmInitRequest request) {
    KeyPath keyPath = path.subPath("init");
    Response response = client.post(keyPath, request, patterns);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return StringEncodings.encodeUtf8(response.content());
  }

  public CompletableFuture<Swarm> inspectSwarm() {
    CompletableFuture<Swarm> future = new CompletableFuture<>();
    executor.execute(() -> inspectAndComplete(future));
    return future;
  }

  private void inspectAndComplete(CompletableFuture<Swarm> future) {
    Swarm swarm = inspectBlocking();
    if (swarm == null) {
      String errorMessage =
        "The swarm could not be inspected from the servers, "
          + "please check if the name of the swarm exists or if there"
          + "are any server errors.";
      future.completeExceptionally(SwarmException.withMessage(errorMessage));
      return;
    }
    future.complete(swarm);
  }

  @Nullable
  private Swarm inspectBlocking() {
    Response response = client.get(path);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeSwarm(response.content());
  }

  private Swarm mergeSwarm(InputStream inputStream) {
    Swarm.Builder builder = Swarm.newBuilder();
    patterns.readMessage(inputStream, builder);
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