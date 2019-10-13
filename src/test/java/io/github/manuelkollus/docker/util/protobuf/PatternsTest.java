package io.github.manuelkollus.docker.util.protobuf;

import com.google.common.base.Charsets;
import io.github.manuelkollus.docker.swarm.Version;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public final class PatternsTest {

  @Test
  public void testMessageWriteConsistency() {
    Version version = Version.newBuilder()
      .setIndex(1)
      .build();
    Patterns versionPatterns = Patterns.newBuilder()
      .addPattern("index", "Index")
      .create();
    String text = versionPatterns.write(version);
    Assert.assertEquals(text, "{\"Index\": 1}");
  }

  @Test
  public void testReadConsistency() {
    Patterns versionPatterns = Patterns.newBuilder()
      .addPattern("index", "Index")
      .create();
    InputStream inputStream = decodeString("{\"Index\": 1}");
    testReadConsistency(inputStream, versionPatterns);
  }

  private void testReadConsistency(InputStream inputStream, Patterns patterns) {
    Version.Builder builder = Version.newBuilder();
    patterns.readMessage(inputStream, builder);
    Version version = builder.build();
    Assert.assertEquals(version.getIndex(), 1);
  }

  private InputStream decodeString(String string) {
    return IOUtils.toInputStream(string, Charsets.UTF_8);
  }
}