package io.github.manuelkollus.docker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public final class HttpRequests {
  private HttpRequests() {}

  public static String get(HttpClient client, KeyPath path) {
    HttpGet request = new HttpGet(path.value());
    request.addHeader("accept", "application/json");
    try {
      HttpResponse response = client.execute(request);
      InputStream inputStream = response.getEntity().getContent();
      return StringEncodings.encodeUtf8(inputStream, StandardCharsets.UTF_8);
    } catch (IOException httpExecutionFailure) {
      httpExecutionFailure.printStackTrace();
    }
    return null;
  }
}
