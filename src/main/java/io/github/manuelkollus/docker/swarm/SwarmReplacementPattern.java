package io.github.manuelkollus.docker.swarm;

import java.util.HashMap;
import java.util.Map;

public final class SwarmReplacementPattern {
  private static final class Lazy {
    private static final Map<String, String> PATTERNS = collectPatterns();
  }

  private SwarmReplacementPattern() {}

  private static Map<String, String> collectPatterns() {
    Map<String, String> patterns = new HashMap<>();
    patterns.put("createdAt", "CreatedAt");
    patterns.put("spec", "Spec");
    patterns.put("heartbeatPeriod", "HeartbeatPeriod");
    patterns.put("orchestration", "Orchestration");
    patterns.put("taskHistoryRetentionLimit", "TaskHistoryRetentionLimit");
    patterns.put("caConfig", "CAConfig");
    patterns.put("nodeCertExpiry", "NodeCertExpiry");
    patterns.put("raft", "Raft");
    patterns.put("logEntriesForSlowFollowers", "LogEntriesForSlowFollowers");
    patterns.put("heartbeatTick", "HeartbeatTick");
    patterns.put("snapshotInterval", "SnapshotInterval");
    patterns.put("electionTick", "ElectionTick");
    patterns.put("taskDefaults", "TaskDefaults");
    patterns.put("encryptionConfig", "EncryptionConfig");
    patterns.put("autoLockManagers", "AutoLockManagers");
    patterns.put("name", "Name");
    patterns.put("joinTokens", "JoinTokens");
    patterns.put("worker", "Worker");
    patterns.put("manager", "Manager");
    patterns.put("id", "ID");
    patterns.put("updatedAt", "UpdatedAt");
    patterns.put("version", "Version");
    patterns.put("index", "Index");
    patterns.put("rootRotationInProgress", "RootRotationInProgress");
    patterns.put("tlsInfo", "TLSInfo");
    patterns.put("trustRoot", "TrustRoot");
    patterns.put("certIssuerSubject", "CertIssuerSubject");
    patterns.put("certIssuerPublicKey", "CertIssuerPublicKey");
    return patterns;
  }

  public static Map<String, String> patterns() {
    return Lazy.PATTERNS;
  }
}
