package io.github.manuelkollus.docker.swarm;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface SwarmRepository {
  CompletableFuture<?> create(Swarm swarm);

  CompletableFuture<?> delete(String swarmId);

  CompletableFuture<?> update(Swarm swarm);

  CompletableFuture<Swarm> find(String swarmId);

  CompletableFuture<Collection<Swarm>> findAll();
}
