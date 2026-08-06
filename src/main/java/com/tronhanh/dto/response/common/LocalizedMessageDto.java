package com.tronhanh.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO containing localized message text for Vietnamese (VI) and English (EN).
 */
@Schema(description = "Localized message container supporting Vietnamese and English")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedMessageDto
{

  /**
   * Message text in Vietnamese.
   */
  @Schema(description = "Vietnamese localized message text", example = "Thao tác thành công.")
  private String vi;

  /**
   * Message text in English.
   */
  @Schema(description = "English localized message text", example = "Operation successful.")
  private String en;
}
