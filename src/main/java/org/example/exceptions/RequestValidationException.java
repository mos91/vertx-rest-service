package org.example.exceptions;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class RequestValidationException extends RuntimeException {

  public RequestValidationException() {
    super();
  }

  public RequestValidationException(String message) {
    super(message);
  }

  public RequestValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestValidationException(Throwable cause) {
    super(cause);
  }
}
