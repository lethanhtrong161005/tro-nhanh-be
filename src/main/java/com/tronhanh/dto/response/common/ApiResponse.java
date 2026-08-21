package com.tronhanh.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized API Response envelope wrapping response data, metadata, status, and message.
 *
 * @param <T> Response payload data type.
 */
@Schema(description = "Standardized envelope response wrapper for all REST API endpoints")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  /** HTTP status code (e.g., 200, 400, 401, 500). */
  @Schema(description = "HTTP status code", example = "200")
  private int status;

  /** Message code constant (e.g., MSG_CODE_001). */
  @Schema(description = "Message code constant key", example = "MSG_CODE_001")
  private String messageCode;

  /** Localized user-friendly display message. */
  @Schema(description = "Localized human-readable result message", example = "Thao tác thành công.")
  private String message;

  /** Response payload data object. */
  @Schema(description = "Main response payload data")
  private T data;

  /** ISO-8601 response generation timestamp. */
  @Schema(
      description = "ISO-8601 timestamp when response was generated",
      example = "2026-08-06T15:52:00")
  @Builder.Default
  private Instant timestamp = Instant.now();

  /** MDC Trace ID for tracing and debugging requests. */
  @Schema(description = "MDC Trace ID for distributed tracing", example = "a1b2c3d4-e5f6-7890")
  private String traceId;

  /** Request endpoint path. */
  @Schema(description = "Request endpoint path", example = "/api/v1/auth/login")
  private String path;
}
