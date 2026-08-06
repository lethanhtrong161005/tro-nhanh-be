package com.tronhanh.exception;

import com.tronhanh.dto.response.common.LocalizedMessageDto;
import java.util.Objects;
import lombok.Getter;

/**
 * Custom runtime exception carrying HTTP status code and localized error message DTO.
 */
@Getter
public class HttpException extends RuntimeException
{

  private final int statusCode;
  private final LocalizedMessageDto localizedMessageDto;

  /**
   * HttpException constructor.
   *
   * @param statusCode HTTP status integer
   * @param localizedMessageDto localized message payload
   */
  public HttpException(int statusCode, LocalizedMessageDto localizedMessageDto) {
    super(
        Objects.nonNull(localizedMessageDto) && Objects.nonNull(localizedMessageDto.getEn())
            ? localizedMessageDto.getEn()
            : "HTTP Exception");
    this.statusCode = statusCode;
    this.localizedMessageDto = localizedMessageDto;
  }
}
