package com.tronhanh.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Specialized response envelope returned when {@code @Valid} bean validation fails on a request
 * DTO. Instead of a single {@code messageCode} and {@code data}, this response exposes a structured
 * list of {@link ValidationErrorItem} objects, one per violated constraint.
 */
@Schema(description = "Response envelope returned when request DTO bean validation fails.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

  /** HTTP status code (always 400 for validation failures). */
  @Schema(description = "HTTP status code", example = "400")
  private int status;

  /**
   * Structured list of field-level validation error items. Each entry carries its own messageCode
   * and localized message.
   */
  @Schema(description = "List of field-level validation error items")
  private List<ValidationErrorItem> errors;

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
  @Schema(description = "Request endpoint path", example = "/api/v1/auth/resend-otp")
  private String path;
}
