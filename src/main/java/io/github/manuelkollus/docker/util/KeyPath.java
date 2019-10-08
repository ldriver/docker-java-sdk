package io.github.manuelkollus.docker.util;

public interface KeyPath {
  KeyPath subPath(String value);

  String value();
}