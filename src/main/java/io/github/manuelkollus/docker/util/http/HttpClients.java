package io.github.manuelkollus.docker.util.http;

import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import io.github.manuelkollus.docker.util.KeyPath;
import io.github.manuelkollus.docker.util.protobuf.MessageWriter;
import io.github.manuelkollus.docker.util.protobuf.Patterns;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

public final class HttpClients {
  private HttpClient client;
  private MessageWriter writer;

  @Inject
  private HttpClients(HttpClient client, MessageWriter writer) {
    this.client = client;
    this.writer = writer;
  }

  public Response post(
    KeyPath path, GeneratedMessage message, Patterns patterns) {
    String text = writer.writeGeneratedMessage(message, patterns);
    HttpPost postRequest = new HttpPost(path.value());
    StringEntity entity = tryEncodeStringToJsonEntity(text);
    postRequest.setEntity(entity);
    return tryExecuteRequest(client, postRequest);
  }

  public Response get(KeyPath path) {
    HttpGet request = new HttpGet(path.value());
    request.addHeader("accept", "application/json");
    return tryExecuteRequest(client, request);
  }

  private Response tryExecuteRequest(
    HttpClient client, HttpUriRequest request) {
    try {
      HttpResponse response = client.execute(request);
      return tryBuildResponse(response);
    } catch (IOException httpExecutionFailure) {
      httpExecutionFailure.printStackTrace();
    }
    return null;
  }

  private Response tryBuildResponse(HttpResponse response) {
    try {
      HttpEntity httpEntity = response.getEntity();
      StatusLine statusLine = response.getStatusLine();
      return Response.create(
        httpEntity.getContent(), statusLine.getStatusCode());
    } catch (IOException contentAccessFailure) {
      contentAccessFailure.printStackTrace();
    }
    return null;
  }

  private StringEntity tryEncodeStringToJsonEntity(String text) {
    if (isNullOrEmpty(text)) {
      return null;
    }
    return new StringEntity(text, Charsets.UTF_8);
  }

  private boolean isNullOrEmpty(String text) {
    return text == null || text.isEmpty();
  }
}