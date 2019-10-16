package io.github.manuelkollus.docker.util.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import io.github.manuelkollus.docker.util.StringEncodings;
import io.github.manuelkollus.docker.util.Strings;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Patterns {
  private List<Pattern> patterns;
  private JsonFormat format;

  private Patterns(List<Pattern> patterns) {
    this.patterns = patterns;
    this.format = new JsonFormat();
  }

  public String write(GeneratedMessage message) {
    if (isNullOrNotInitialized(message)) {
      return "";
    }
    String text = format.printToString(message);
    return replaceValue(text);
  }

  private boolean isNullOrNotInitialized(GeneratedMessage message) {
    return message == null || !message.isInitialized();
  }

  public void readMessage(
    InputStream inputStream, GeneratedMessage.Builder builder) {
    if (isNullOrNotInitialized(builder)) {
      return;
    }
    try {
      String text = encodeAndReplaceText(inputStream);
      format.merge(text, ExtensionRegistry.getEmptyRegistry(), builder);
    } catch (ParseException textMergeFailure) {
      textMergeFailure.printStackTrace();
    }
  }

  private String encodeAndReplaceText(InputStream inputStream) {
    String text = StringEncodings.encodeUtf8(inputStream);
    for (Pattern pattern : patterns) {
      text = pattern.replace(text, PatternReplacementStrategy.VALUE);
    }
    return text;
  }

  public String replaceValue(String value) {
    for (Pattern pattern : patterns) {
      value = pattern.replace(value, PatternReplacementStrategy.KEY);
    }
    return value;
  }

  private boolean isNullOrNotInitialized(GeneratedMessage.Builder builder) {
    return builder == null || !builder.isInitialized();
  }

  public static Builder newBuilder() {
    return new Builder(new ArrayList<>());
  }

  public static final class Builder {
    private List<Pattern> patterns;

    private Builder(List<Pattern> patterns) {
      this.patterns = patterns;
    }

    private Builder addPattern(Pattern pattern) {
      Objects.requireNonNull(pattern);
      this.patterns.add(pattern);
      return this;
    }

    public Builder addPattern(String value) {
      return addPattern(value, Strings.capitalize(value));
    }

    public Builder addPattern(String key, String value) {
      Pattern pattern = Pattern.create(key, value);
      return addPattern(pattern);
    }

    public Patterns create() {
      return new Patterns(patterns);
    }
  }
}