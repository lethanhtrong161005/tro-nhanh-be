package com.tronhanh.dto.specification;

import com.tronhanh.enums.SearchOperation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object encapsulating a single filter criterion for dynamic specification queries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter criterion payload for dynamic database queries")
public class FilterCriteria {

  @Schema(description = "Entity attribute path (supports nested paths like 'user.fullName')", example = "fullName")
  private String key;

  @Schema(description = "Criteria operation type", example = "LIKE")
  private SearchOperation operation;

  @Schema(description = "Filter target value", example = "John")
  private Object value;
}
