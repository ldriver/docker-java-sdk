package io.github.manuelkollus.docker.util;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Base64;

public final class StringEncodings {
  private StringEncodings() {}

  public static byte[] encodeUtf8(String value) {
    return value.getBytes(Charsets.UTF_8);
  }

  public static String decodeUtf8(byte[] buffer) {
    return new String(buffer, Charsets.UTF_8);
  }

  public static String encodeUtf8(InputStream inputStream) {
    try {
      Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8);
      return CharStreams.toString(reader);
    } catch (IOException convertFailure) {
      convertFailure.printStackTrace();
    }
    return null;
  }

  private static final Base64.Encoder ENCODER = Base64.getEncoder();

  public static String encodeToBase64(String value) {
    byte[] bytes = encodeUtf8(value);
    return ENCODER.encodeToString(bytes);
  }
}