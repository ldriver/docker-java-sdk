package io.github.manuelkollus.docker.system;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.StringEncodings;
import io.github.manuelkollus.docker.util.http.HttpClients;
import io.github.manuelkollus.docker.util.http.Response;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class SystemRepository {
  private KeyPath path;
  private Executor executor;
  private Patterns patterns;
  private HttpClients client;

  @Inject
  private SystemRepository(
    KeyPath path,
    Executor executor,
    @Named("System") Patterns patterns,
    HttpClients client) {
    this.path = path;
    this.executor = executor;
    this.patterns = patterns;
    this.client = client;
  }

  public CompletableFuture<Boolean> accessible() {
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    executor.execute(() -> accessibleAndComplete(future));
    return future;
  }

  private void accessibleAndComplete(CompletableFuture<Boolean> future) {
    boolean accessible = accessibleBlocking();
    future.complete(accessible);
  }

  private boolean accessibleBlocking() {
    KeyPath keyPath = path.subPath("_ping");
    Response response = client.get(keyPath);
    if (isRequestFailed(SERVER_ERROR)) {
      return false;
    }
    return accessibleByContent(response);
  }

  private boolean accessibleByContent(Response response) {
    String text = StringEncodings.encodeUtf8(response.content());
    return text.equals("OK");
  }

  private static final int SERVER_ERROR = 500;

  private boolean isRequestFailed(int code) {
    return code == SERVER_ERROR;
  }
}
