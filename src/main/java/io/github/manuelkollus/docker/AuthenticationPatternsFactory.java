package io.github.manuelkollus.docker;

import io.github.manuelkollus.docker.util.protobuf.Patterns;
import io.github.manuelkollus.docker.util.protobuf.PatternsFactory;

public final class AuthenticationPatternsFactory implements PatternsFactory {
  private AuthenticationPatternsFactory() {}

  @Override
  public Patterns createPatterns() {
    return Patterns.newBuilder()
      .addPattern("name", "username")
      .addPattern("password", "password")
      .addPattern("email", "email")
      .addPattern("address", "serveraddress")
      .create();
  }

  public static PatternsFactory create() {
    return new AuthenticationPatternsFactory();
  }
}
