package com.tronhanh.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single field-level validation error item returned inside a {@link
 * ValidationErrorResponse}. Each item maps one violation to its localized message.
 */
@Schema(description = "Single field-level validation error item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorItem {

  /**
   * The message code constant key for this specific validation violation. Example: MSG_CODE_200 for
   * a required field error.
   */
  @Schema(description = "Message code constant key for this violation", example = "MSG_CODE_200")
  private String messageCode;

  /**
   * Localized, human-readable description of the validation violation. Example: "Session ID is
   * required."
   */
  @Schema(
      description = "Human-readable validation error message",
      example = "Session ID is required.")
  private String message;
}
