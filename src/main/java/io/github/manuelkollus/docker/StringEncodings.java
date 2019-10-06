package io.github.manuelkollus.docker;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public final class StringEncodings {
  private StringEncodings() {}

  public static byte[] encodeUtf8(String value) {
    return value.getBytes(Charsets.UTF_8);
  }

  public static String decodeUtf8(byte[] buffer) {
    return new String(buffer, Charsets.UTF_8);
  }

  public static String convertFromInputStream(
    InputStream inputStream,
    Charset charset
  ) {
    try {
      return CharStreams.toString(new InputStreamReader(inputStream, charset));
    } catch (IOException convertFailure) {
      convertFailure.printStackTrace();
    }
    return null;
  }
}