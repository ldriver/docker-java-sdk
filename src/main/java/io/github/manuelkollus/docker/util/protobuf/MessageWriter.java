package io.github.manuelkollus.docker.util.protobuf;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.GeneratedMessage;
import com.googlecode.protobuf.format.JsonFormat;
import java.util.Collection;
import java.util.Collections;

public final class MessageWriter {
  private JsonFormat format;

  private MessageWriter(JsonFormat format) {
    this.format = format;
  }

  public String writeGeneratedMessage(
    GeneratedMessage message, Patterns patterns) {
    if (isNullOrNotInitialized(message)) {
      return "";
    }
    Collection<Pattern> patternCollection = patterns.patterns();
    if (isNullOrImmutable(patternCollection)) {
      return writeMessageAndReplaceText(message, Collections.emptyList());
    }
    return writeMessageAndReplaceText(message, patternCollection);
  }

  private String writeMessageAndReplaceText(
    GeneratedMessage message, Collection<Pattern> patterns) {
    String text = format.printToString(message);
    return replaceTextWithPatterns(text, patterns);
  }

  private String replaceTextWithPatterns(
    String text, Collection<Pattern> patterns) {
    for (Pattern pattern : patterns) {
      text = pattern.replace(text, PatternReplacementStrategy.KEY);
    }
    return text;
  }

  private boolean isNullOrNotInitialized(GeneratedMessage message) {
    return message == null || !message.isInitialized();
  }

  private boolean isNullOrImmutable(Collection<Pattern> patterns) {
    return patterns == null || patterns instanceof ImmutableList;
  }

  public static MessageWriter create(JsonFormat format) {
    return new MessageWriter(format);
  }
}
