package io.github.manuelkollus.docker.system;

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

  public CompletableFuture<AuthResponse> checkAuth(AuthRequest request) {
    CompletableFuture<AuthResponse> future = new CompletableFuture<>();
    executor.execute(() -> checkAuthAndComplete(request, future));
    return future;
  }

  private void checkAuthAndComplete(
    AuthRequest request, CompletableFuture<AuthResponse> future) {
    AuthResponse response = checkAuthBlocking(request);
    future.complete(response);
  }

  @Nullable
  private AuthResponse checkAuthBlocking(AuthRequest request) {
    KeyPath keyPath = path.subPath("auth");
    Response response = client.post(keyPath, request, patterns);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeAuthResponse(response.content());
  }

  private AuthResponse mergeAuthResponse(InputStream inputStream) {
    AuthResponse.Builder builder = AuthResponse.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  public CompletableFuture<SystemInfo> findSystemInfo() {
    CompletableFuture<SystemInfo> future = new CompletableFuture<>();
    executor.execute(() -> findSystemInfoAndComplete(future));
    return future;
  }

  private void findSystemInfoAndComplete(CompletableFuture<SystemInfo> future) {
    SystemInfo systemInfo = findSystemInfoBlocking();
    future.complete(systemInfo);
  }

  @Nullable
  private SystemInfo findSystemInfoBlocking() {
    KeyPath keyPath = path.subPath("info");
    Response response = client.get(keyPath);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeSystemInfo(response.content());
  }

  private SystemInfo mergeSystemInfo(InputStream inputStream) {
    SystemInfo.Builder builder = SystemInfo.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  public CompletableFuture<SystemVersion> findVersion() {
    CompletableFuture<SystemVersion> future = new CompletableFuture<>();
    executor.execute(() -> findVersionAndComplete(future));
    return future;
  }

  private void findVersionAndComplete(CompletableFuture<SystemVersion> future) {
    SystemVersion version = findVersionBlocking();
    future.complete(version);
  }

  @Nullable
  private SystemVersion findVersionBlocking() {
    KeyPath keyPath = path.subPath("version");
    Response response = client.get(keyPath);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeVersion(response.content());
  }

  private SystemVersion mergeVersion(InputStream inputStream) {
    SystemVersion.Builder builder = SystemVersion.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
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
    if (isRequestFailed(response.code())) {
      return false;
    }
    return validateAccessible(response);
  }

  private boolean validateAccessible(Response response) {
    String value = StringEncodings.encodeUtf8(response.content());
    if (isNullOrEmpty(value)) {
      return false;
    }
    return value.equals("OK");
  }

  private boolean isNullOrEmpty(String value) {
    return value == null || value.isEmpty();
  }

  private static final int SERVER_ERROR = 500;

  private boolean isRequestFailed(int code) {
    return code == SERVER_ERROR;
  }
}
