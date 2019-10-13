package io.github.manuelkollus.docker.swarm;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class SwarmPatternsFactory implements PatternsFactory {
  private SwarmPatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    Patterns.Builder builder = Patterns.newBuilder();
    addSwarmPatterns(builder);
    addSpecPatterns(builder);
    addEncryptionConfigPatterns(builder);
    addJoinTokensPatterns(builder);
    addVersionPatterns(builder);
    addTlsInfoPatterns(builder);
    return builder.create();
  }

  private void addSwarmPatterns(Patterns.Builder builder) {
    builder.addPattern("createdAt")
      .addPattern("name")
      .addPattern("id", "ID")
      .addPattern("updatedAt")
      .addPattern("rootRotationInProgress")
      .addPattern("taskDefaults");
  }

  private void addSpecPatterns(Patterns.Builder builder) {
    builder.addPattern("spec")
      .addPattern("dispatcher")
      .addPattern("heartbeatPeriod")
      .addPattern("taskHistoryRetentionLimit")
      .addPattern("caConfig", "CAConfig")
      .addPattern("nodeCertExpiry")
      .addPattern("raft")
      .addPattern("logEntriesForSlowFollowers")
      .addPattern("heartbeatTick")
      .addPattern("snapshotInterval")
      .addPattern("electionTick");
  }

  private void addEncryptionConfigPatterns(Patterns.Builder builder) {
    builder.addPattern("encryptionConfig")
      .addPattern("autoLockManagers");
  }

  private void addJoinTokensPatterns(Patterns.Builder builder) {
    builder.addPattern("joinTokens")
      .addPattern("worker")
      .addPattern("manager");
  }

  private void addVersionPatterns(Patterns.Builder builder) {
    builder.addPattern("version")
      .addPattern("index");
  }

  private void addTlsInfoPatterns(Patterns.Builder builder) {
    builder.addPattern("tlsInfo", "TLSInfo")
      .addPattern("trustRoot")
      .addPattern("certIssuerSubject")
      .addPattern("certIssuerPublicKey");
  }


  public static PatternsFactory create() {
    return new SwarmPatternsFactory();
  }
}
