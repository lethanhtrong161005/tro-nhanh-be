package com.tronhanh.exception;

import com.tronhanh.util.MessageUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom runtime exception carrying HTTP status code, message code, parameters, and English error message.
 */
@Getter
public class HttpException extends RuntimeException
{

  private final int statusCode;
  private final String messageCode;
  private final Object[] args;

  /**
   * Constructs HttpException with HttpStatus enum and message code.
   *
   * @param status HTTP status enum.
   * @param messageCode Message bundle key string.
   */
  public HttpException(HttpStatus status, String messageCode) {
    this(status.value(), messageCode, (Object[]) null);
  }

  /**
   * Constructs HttpException with HttpStatus enum, message code, and parameters.
   *
   * @param status HTTP status enum.
   * @param messageCode Message bundle key string.
   * @param args Parameters for message placeholder substitution.
   */
  public HttpException(HttpStatus status, String messageCode, Object... args) {
    this(status.value(), messageCode, args);
  }

  /**
   * Constructs HttpException with status integer and message code.
   *
   * @param statusCode HTTP status integer.
   * @param messageCode Message bundle key string.
   */
  public HttpException(int statusCode, String messageCode) {
    this(statusCode, messageCode, (Object[]) null);
  }

  /**
   * Constructs HttpException with status integer, message code, and parameters.
   *
   * @param statusCode HTTP status integer.
   * @param messageCode Message bundle key string.
   * @param args Parameters for message placeholder substitution.
   */
  public HttpException(int statusCode, String messageCode, Object... args) {
    super(MessageUtils.getMessage(messageCode, args));
    this.statusCode = statusCode;
    this.messageCode = messageCode;
    this.args = args;
  }
}
