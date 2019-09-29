package io.github.manuelkollus.docker;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class ObjectSchema<T> {
  private Supplier<T> factory;
  private Map<String, ObjectSchemaField<T, ?>> fields;

  private ObjectSchema(
    Supplier<T> factory,
    Map<String, ObjectSchemaField<T, ?>> fields
  ) {
    this.factory = factory;
    this.fields = fields;
  }

  public Supplier<T> factory() {
    return this.factory;
  }

  public Map<String, ObjectSchemaField<T, ?>> fields() {
    return this.fields;
  }

  public static <T> Builder<T> newBuilder() {
    return new Builder<>(null, new HashMap<>());
  }

  public static final class Builder<T> {
    private Supplier<T> factory;
    private Map<String, ObjectSchemaField<T, ?>> fields;

    private Builder(
      Supplier<T> factory,
      Map<String, ObjectSchemaField<T, ?>> fields
    ) {
      this.factory = factory;
      this.fields = fields;
    }

    public Builder<T> withFactory(Supplier<T> factory) {
      Objects.requireNonNull(factory);
      this.factory = factory;
      return this;
    }

    public Builder<T> addField(ObjectSchemaField<T, ?> field) {
      Objects.requireNonNull(field);
      fields.put(field.name(), field);
      return this;
    }

    public ObjectSchema<T> create() {
      Objects.requireNonNull(factory);
      return new ObjectSchema<>(
        factory,
        ImmutableMap.copyOf(fields)
      );
    }
  }
}
