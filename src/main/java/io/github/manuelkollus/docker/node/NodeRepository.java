package io.github.manuelkollus.docker.node;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.http.HttpClients;
import io.github.manuelkollus.docker.util.http.Response;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public final class NodeRepository {
  private KeyPath path;
  private Executor executor;
  private Patterns patterns;
  private HttpClients client;

  @Inject
  private NodeRepository(
    KeyPath path,
    Executor executor,
   @Named("Node") Patterns patterns,
    HttpClients client
  ) {
    this.path = path.subPath("nodes");
    this.executor = executor;
    this.patterns = patterns;
    this.client = client;
  }

  public CompletableFuture<Node> inspectNode(String nodeId) {
    CompletableFuture<Node> future = new CompletableFuture<>();
    executor.execute(() -> inspectNodeAndComplete(nodeId, future));
    return future;
  }

  private void inspectNodeAndComplete(
    String nodeId, CompletableFuture<Node> future) {
    Node node = inspectNodeBlocking(nodeId);
    future.complete(node);
  }

  @Nullable
  private Node inspectNodeBlocking(String nodeId) {
    KeyPath keyPath = path.subPath(nodeId);
    Response response = client.get(keyPath);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeNode(response.content());
  }

  private Node mergeNode(InputStream inputStream) {
    Node.Builder builder = Node.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  public CompletableFuture<Boolean> deleteNode(String name) {
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    executor.execute(() -> deleteNodeAndComplete(name, future));
    return future;
  }

  private void deleteNodeAndComplete(
    String name, CompletableFuture<Boolean> future) {
    Boolean removed = deleteNodeBlocking(name);
    future.complete(removed);
  }

  private Boolean deleteNodeBlocking(String name) {
    KeyPath keyPath = path.subPath(name);
    Response response = client.delete(keyPath);
    return !isRequestFailed(response.code());
  }

  public CompletableFuture<Nodes> findNodes() {
    CompletableFuture<Nodes> future = new CompletableFuture<>();
    executor.execute(() -> findNodesAndComplete(future));
    return future;
  }

  private void findNodesAndComplete(CompletableFuture<Nodes> future) {
    Nodes nodes = findNodesBlocking();
    future.complete(nodes);
  }

  @Nullable
  private Nodes findNodesBlocking() {
    Response response = client.get(path);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeNodes(response.content());
  }

  private Nodes mergeNodes(InputStream inputStream) {
    Nodes.Builder builder = Nodes.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  private static final int NO_SUCH_NODE = 404;
  private static final int SERVER_ERROR = 500;
  private static final int NOT_PART_OF_SWARM = 503;

  private boolean isRequestFailed(int code) {
    return code == NO_SUCH_NODE ||
      code == SERVER_ERROR ||
      code == NOT_PART_OF_SWARM;
  }
}
