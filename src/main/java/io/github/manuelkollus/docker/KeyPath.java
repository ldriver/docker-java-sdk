package io.github.manuelkollus.docker;

public interface KeyPath {
  KeyPath subPath(String value);

  String value();
}