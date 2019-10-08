package io.github.manuelkollus.docker.util.http;

import java.io.InputStream;
import java.util.Objects;

public final class Response {
  private InputStream content;
  private int code;

  private Response(InputStream content, int code) {
    this.content = content;
    this.code = code;
  }

  public InputStream content() {
    return this.content;
  }

  public int code() {
    return this.code;
  }

  public static Response create(InputStream content, int code) {
    Objects.requireNonNull(content);
    return new Response(content, code);
  }
}
