package io.github.manuelkollus.docker.swarm;

import io.github.manuelkollus.docker.util.Strings;
import io.github.manuelkollus.docker.util.protobuf.Pattern;
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
    builder.addPattern(create("createdAt"))
      .addPattern(create("name"))
      .addPattern(create("id", "ID"))
      .addPattern(create("updatedAt"))
      .addPattern(create("rootRotationInProgress"))
      .addPattern(create("taskDefaults"));
  }

  private void addSpecPatterns(Patterns.Builder builder) {
    builder.addPattern(create("spec"))
      .addPattern(create("dispatcher"))
      .addPattern(create("heartbeatPeriod"))
      .addPattern(create("taskHistoryRetentionLimit"))
      .addPattern(create("caConfig", "CAConfig"))
      .addPattern(create("nodeCertExpiry"))
      .addPattern(create("raft"))
      .addPattern(create("logEntriesForSlowFollowers"))
      .addPattern(create("heartbeatTick"))
      .addPattern(create("snapshotInterval"))
      .addPattern(create("electionTick"));
  }

  private void addEncryptionConfigPatterns(Patterns.Builder builder) {
    builder.addPattern(create("encryptionConfig"))
      .addPattern(create("autoLockManagers"));
  }

  private void addJoinTokensPatterns(Patterns.Builder builder) {
    builder.addPattern(create("joinTokens"))
      .addPattern(create("worker"))
      .addPattern(create("manager"));
  }

  private void addVersionPatterns(Patterns.Builder builder) {
    builder.addPattern(create("version"))
      .addPattern(create("index"));
  }

  private void addTlsInfoPatterns(Patterns.Builder builder) {
    builder.addPattern(create("tlsInfo", "TLSInfo"))
      .addPattern(create("trustRoot"))
      .addPattern(create("certIssuerSubject"))
      .addPattern(create("certIssuerPublicKey"));
  }

  private Pattern create(String value) {
    return create(value, Strings.capitalize(value));
  }

  private Pattern create(String key, String value) {
    return Pattern.create(key, value);
  }

  public static PatternsFactory create() {
    return new SwarmPatternsFactory();
  }
}
