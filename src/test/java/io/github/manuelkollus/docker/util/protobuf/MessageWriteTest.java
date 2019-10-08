package io.github.manuelkollus.docker.util.protobuf;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.protobuf.GeneratedMessage;
import io.github.manuelkollus.docker.DockerConfig;
import io.github.manuelkollus.docker.InjectModule;
import io.github.manuelkollus.docker.swarm.Version;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.junit.Assert;
import org.junit.Test;

public final class MessageWriteTest {

  @Test
  public void testMessageWriteConsistency() {
    Version version = Version.newBuilder()
      .setIndex(1)
      .build();
    Pattern versionIndexPattern = Pattern.create("index", "Index");
    Patterns versionPatterns = Patterns.newBuilder()
      .addPattern(versionIndexPattern)
      .create();
    testMessageWriteConsistency(version, versionPatterns);
  }

  private void testMessageWriteConsistency(
    GeneratedMessage message, Patterns patterns) {
    Injector injector = createInjector();
    MessageWriter writer = injector.getInstance(MessageWriter.class);
    String text = writer.writeGeneratedMessage(message, patterns);
    Assert.assertEquals(text, "{\"Index\": 1}");
  }

  private Injector createInjector() {
    DockerConfig config = DockerConfig.newBuilder()
      .withKeyPath("https://0.0.0.0:2376")
      .withCredentials(new UsernamePasswordCredentials("name", "password"))
      .create();
    InjectModule module = InjectModule.of(config);
    return Guice.createInjector(module);
  }
}
