package io.github.manuelkollus.docker.util.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.JsonFormat.ParseException;
import io.github.manuelkollus.docker.util.StringEncodings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public final class MessageReader {
  private JsonFormat format;

  private MessageReader(JsonFormat format) {
    this.format = format;
  }

  public void readMessage(
    InputStream inputStream,
    GeneratedMessage.Builder builder,
    Patterns patterns
  ) throws IOException {
    if (isNullOrInitialized(builder)) {
      return;
    }
    Collection<Pattern> patternCollection = patterns.patterns();
    if (isNullOrEmpty(patternCollection)) {
      format.merge(inputStream, builder);
      return;
    }
    tryReadMessage(inputStream, builder, patternCollection);
  }

  private void tryReadMessage(
    InputStream inputStream,
    GeneratedMessage.Builder builder,
    Collection<Pattern> patterns
  ) {
    String text = encodeTextAndReplacePatterns(inputStream, patterns);
    try {
      format.merge(
        text,
        ExtensionRegistry.getEmptyRegistry(),
        builder
      );
    } catch (ParseException textMergeFailure) {
      textMergeFailure.printStackTrace();
    }
  }

  private String encodeTextAndReplacePatterns(
    InputStream inputStream, Collection<Pattern> patterns) {
    String text = StringEncodings.encodeUtf8(inputStream);
    return replaceTextWithPatterns(text, patterns);
  }

  private String replaceTextWithPatterns(
    String text, Collection<Pattern> patterns) {
    for (Pattern pattern : patterns) {
      text = pattern.replace(text);
    }
    return text;
  }

  private boolean isNullOrInitialized(GeneratedMessage.Builder builder) {
    return builder == null || builder.isInitialized();
  }

  private boolean isNullOrEmpty(Collection<Pattern> patterns) {
    return patterns == null || patterns.isEmpty();
  }

  public static MessageReader create(JsonFormat format) {
    return new MessageReader(format);
  }
}
