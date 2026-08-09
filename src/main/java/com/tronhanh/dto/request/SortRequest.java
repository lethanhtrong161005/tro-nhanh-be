package com.tronhanh.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized request payload for sorting parameters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Sort condition configuration payload")
public class SortRequest {
  
  @Schema(description = "Sort direction (ASC or DESC)", example = "DESC")
  private String sortOrder;
}
