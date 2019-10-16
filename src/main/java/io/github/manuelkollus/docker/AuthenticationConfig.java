package io.github.manuelkollus.docker;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.manuelkollus.docker.util.StringEncodings;
import java.util.Objects;

public final class AuthenticationConfig {
  @JsonProperty("username")
  private String name;
  private String password;
  private String email;
  @JsonProperty("serveraddress")
  private String address;
  @JsonIgnore
  private ObjectMapper objectMapper;

  private AuthenticationConfig() {}

  private AuthenticationConfig(
    String name,
    String password,
    String email,
    String address
  ) {
    this.name = name;
    this.password = password;
    this.email = email;
    this.address = address;
    this.objectMapper = createObjectMapper();
  }

  public String tryEncodeToBase64() {
    try {
      String value = objectMapper.writeValueAsString(this);
      return StringEncodings.encodeToBase64(value);
    } catch (JsonProcessingException writeValueFailure) {
      writeValueFailure.printStackTrace();
    }
    return null;
  }

  private ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    return objectMapper;
  }


  public static AuthenticationConfig.Builder newBuilder() {
    return new AuthenticationConfig.Builder(new AuthenticationConfig());
  }

  public static final class Builder {
    private AuthenticationConfig prototype;

    private Builder(AuthenticationConfig prototype) {
      this.prototype = prototype;
    }

    public Builder withName(String name) {
      Objects.requireNonNull(name);
      this.prototype.name = name;
      return this;
    }

    public Builder withPassword(String password) {
      Objects.requireNonNull(password);
      this.prototype.password = password;
      return this;
    }

    public Builder withEmail(String email) {
      Objects.requireNonNull(email);
      this.prototype.email = email;
      return this;
    }

    public Builder withAddress(String address) {
      Objects.requireNonNull(address);
      this.prototype.address = address;
      return this;
    }

    public AuthenticationConfig create() {
      return new AuthenticationConfig(
        prototype.name,
        prototype.password,
        prototype.email,
        prototype.address
      );
    }
  }
}
