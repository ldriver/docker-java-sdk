package io.github.manuelkollus.docker.util.protobuf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Patterns {
  private List<Pattern> patterns;

  private Patterns(List<Pattern> patterns) {
    this.patterns = patterns;
  }

  public Collection<Pattern> patterns() {
    return this.patterns;
  }

  public static Builder newBuilder() {
    return new Builder(new ArrayList<>());
  }

  public static final class Builder {
    private List<Pattern> patterns;

    private Builder(List<Pattern> patterns) {
      this.patterns = patterns;
    }

    public Builder addPattern(Pattern pattern) {
      Objects.requireNonNull(pattern);
      this.patterns.add(pattern);
      return this;
    }

    public Patterns create() {
      return new Patterns(patterns);
    }
  }
}
