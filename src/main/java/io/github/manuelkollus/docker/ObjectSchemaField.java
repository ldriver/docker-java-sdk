package io.github.manuelkollus.docker;

import java.util.Objects;

public final class ObjectSchemaField<T, E> {
  public interface Mutator<T, E> {
    void mutate(T type, E value);
  }

  public interface Accessor<T, E> {
    T access(E value);
  }

  private String name;
  private Accessor<T, E> accessor;
  private Mutator<T, E> mutator;

  private ObjectSchemaField() {}

  private ObjectSchemaField(
    String name,
    Accessor<T, E> accessor,
    Mutator<T, E> mutator
  ) {
    this.name = name;
    this.accessor = accessor;
    this.mutator = mutator;
  }

  public String name() {
    return this.name;
  }

  public Accessor<T, E> accessor() {
    return this.accessor;
  }

  public Mutator<T, E> mutator() {
    return this.mutator;
  }

  public static <T, E> Builder<T, E> newBuilder(String name) {
    Objects.requireNonNull(name);
    return new Builder<T, E>(new ObjectSchemaField<>())
      .withName(name);
  }

  public static final class Builder<T, E> {
    private ObjectSchemaField<T, E> prototype;

    private Builder(ObjectSchemaField<T, E> prototype) {
      this.prototype = prototype;
    }

    public Builder<T, E> withName(String name) {
      Objects.requireNonNull(name);
      this.prototype.name = name;
      return this;
    }

    public Builder<T, E> withAccessor(Accessor<T, E> accessor) {
      Objects.requireNonNull(accessor);
      this.prototype.accessor = accessor;
      return this;
    }

    public Builder<T, E> withMutator(Mutator<T, E> mutator) {
      Objects.requireNonNull(mutator);
      this.prototype.mutator = mutator;
      return this;
    }


    public ObjectSchemaField<T, E> create() {
      return new ObjectSchemaField<>(
        prototype.name,
        prototype.accessor,
        prototype.mutator
      );
    }
  }
}