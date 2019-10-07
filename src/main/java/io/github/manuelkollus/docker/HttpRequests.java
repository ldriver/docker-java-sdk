package io.github.manuelkollus.docker;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

public final class HttpRequests {
  private HttpRequests() {
  }

  public static String post(
    HttpClient client,
    KeyPath path,
    String body
  ) {
    HttpPost request = new HttpPost(path.value());
    request.setHeader("Content-type", "application/json");
    try {
      StringEntity entity = new StringEntity(body);
      request.setEntity(entity);
      return executeRequest(client, request);
    } catch (UnsupportedEncodingException encodingFailure) {
      encodingFailure.printStackTrace();
    }
    return null;
  }

  public static String get(HttpClient client, KeyPath path) {
    HttpGet request = new HttpGet(path.value());
    request.addHeader("accept", "application/json");
    return executeRequest(client, request);
  }

  private static String executeRequest(
    HttpClient client,
    HttpUriRequest request
  ) {
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