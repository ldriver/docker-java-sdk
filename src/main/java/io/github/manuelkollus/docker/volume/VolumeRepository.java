package io.github.manuelkollus.docker.volume;

import com.google.inject.name.Named;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.http.HttpClients;
import io.github.manuelkollus.docker.util.http.Response;
import io.github.manuelkollus.docker.util.protobuf.Patterns;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class VolumeRepository {
  private KeyPath path;
  private Executor executor;
  private Patterns patterns;
  private HttpClients client;

  private VolumeRepository(
    KeyPath path,
    Executor executor,
    @Named("Volume") Patterns patterns,
    HttpClients client) {
    this.path = path.subPath("volumes");
    this.executor = executor;
    this.patterns = patterns;
    this.client = client;
  }

  public CompletableFuture<Volume> createVolume(VolumeCreateRequest request) {
    CompletableFuture<Volume> future = new CompletableFuture<>();
    executor.execute(() -> createVolumeAndComplete(request, future));
    return future;
  }

  private void createVolumeAndComplete(
    VolumeCreateRequest request, CompletableFuture<Volume> future) {
    Volume volume = createVolumeBlocking(request);
    future.complete(volume);
  }

  @Nullable
  private Volume createVolumeBlocking(VolumeCreateRequest request) {
    KeyPath keyPath = path.subPath("create");
    Response response = client.post(keyPath, request, patterns);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeVolume(response.content());
  }

  public CompletableFuture<Volume> inspectVolume(String name) {
    CompletableFuture<Volume> future = new CompletableFuture<>();
    executor.execute(() -> inspectVolumeAndComplete(name, future));
    return future;
  }

  private void inspectVolumeAndComplete(
    String name, CompletableFuture<Volume> future) {
    Volume volume = inspectVolumeBlocking(name);
    future.complete(volume);
  }

  @Nullable
  private Volume inspectVolumeBlocking(String name) {
    KeyPath keyPath = path.subPath(name);
    Response response = client.get(keyPath);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeVolume(response.content());
  }

  private Volume mergeVolume(InputStream inputStream) {
    Volume.Builder builder = Volume.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  public CompletableFuture<Boolean> deleteVolume(String name) {
    CompletableFuture<Boolean> future = new CompletableFuture<>();
    executor.execute(() -> deleteVolumeAndComplete(name, future));
    return future;
  }

  private void deleteVolumeAndComplete(
    String name, CompletableFuture<Boolean> future) {
    boolean removed = deleteVolumeBlocking(name);
    future.complete(removed);
  }

  private boolean deleteVolumeBlocking(String name) {
    KeyPath keyPath = path.subPath("delete")
      .subPath(name);
    Response response = client.delete(keyPath);
    return !isRequestFailed(response.code());
  }

  public CompletableFuture<UnusedVolumesResponse> deleteUnusedVolumes() {
    CompletableFuture<UnusedVolumesResponse> future = new CompletableFuture<>();
    executor.execute(() -> deleteUnusedVolumesAndComplete(future));
    return future;
  }

  private void deleteUnusedVolumesAndComplete(
    CompletableFuture<UnusedVolumesResponse> future) {
    UnusedVolumesResponse response = deleteUnusedVolumesBlocking();
    future.complete(response);
  }

  @Nullable
  private UnusedVolumesResponse deleteUnusedVolumesBlocking() {
    KeyPath keyPath = path.subPath("prune");
    Response response = client.post(keyPath, null, null);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeUnusedVolume(response.content());
  }

  private UnusedVolumesResponse mergeUnusedVolume(InputStream inputStream) {
    UnusedVolumesResponse.Builder builder = UnusedVolumesResponse.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }
  public CompletableFuture<Volumes> findVolumes() {
    CompletableFuture<Volumes> future = new CompletableFuture<>();
    executor.execute(() -> findVolumesAndComplete(future));
    return future;
  }

  private void findVolumesAndComplete(CompletableFuture<Volumes> future) {
    Volumes volumes = findVolumesBlocking();
    future.complete(volumes);
  }

  @Nullable
  private Volumes findVolumesBlocking() {
    Response response = client.get(path);
    if (isRequestFailed(response.code())) {
      return null;
    }
    return mergeVolumes(response.content());
  }

  private Volumes mergeVolumes(InputStream inputStream) {
    Volumes.Builder builder = Volumes.newBuilder();
    patterns.readMessage(inputStream, builder);
    return builder.build();
  }

  private static final int NO_SUCH_VOLUME = 404;
  private static final int CANNOT_REMOVE_VOLUME = 409;
  private static final int SERVER_ERROR = 500;

  private boolean isRequestFailed(int code) {
    return code == SERVER_ERROR ||
      code == CANNOT_REMOVE_VOLUME ||
      code == NO_SUCH_VOLUME;
  }
}