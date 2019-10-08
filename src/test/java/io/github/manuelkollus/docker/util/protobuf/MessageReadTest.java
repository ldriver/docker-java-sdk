package io.github.manuelkollus.docker.util.protobuf;

import com.google.common.base.Charsets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.manuelkollus.docker.DockerConfig;
import io.github.manuelkollus.docker.InjectModule;
import io.github.manuelkollus.docker.swarm.Version;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.junit.Assert;
import org.junit.Test;

public final class MessageReadTest {

  @Test
  public void testMessageReadConsistency() {
    Pattern versionIndexPattern = Pattern.create("Index", "index");
    Patterns versionPatterns = Patterns.newBuilder()
      .addPattern(versionIndexPattern)
      .create();
    Version.Builder builder = Version.newBuilder();
    InputStream inputStream = decodeString("{\"Index\": 1}");
    testMessageReadConsistency(inputStream, builder, versionPatterns);
  }

  private void testMessageReadConsistency(
    InputStream inputStream,
    Version.Builder builder,
    Patterns patterns
  ) {
    Injector injector = createInjector();
    MessageReader messageReader = injector.getInstance(MessageReader.class);
    messageReader.readMessage(inputStream, builder, patterns);
    Version version = builder.build();
    Assert.assertEquals(version.getIndex(), 1);
  }


  private Injector createInjector() {
    DockerConfig config = DockerConfig.newBuilder()
      .withKeyPath("https://0.0.0.0:2376")
      .withCredentials(new UsernamePasswordCredentials("name", "password"))
      .create();
    InjectModule module = InjectModule.of(config);
    return Guice.createInjector(module);
  }

  private InputStream decodeString(String string) {
    return IOUtils.toInputStream(string, Charsets.UTF_8);
  }
}
