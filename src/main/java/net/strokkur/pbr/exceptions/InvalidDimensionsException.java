package net.strokkur.pbr.exceptions;

import org.jetbrains.annotations.ApiStatus;

public class InvalidDimensionsException extends RuntimeException {
  @ApiStatus.Internal
  public InvalidDimensionsException(String message) {
    super(message);
  }
}
