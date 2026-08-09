package com.tronhanh.dto.request;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.enums.SortOrder;
import com.tronhanh.validation.EnumValue;
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
@Schema(description = "Base sort condition configuration payload")
public class BaseSortCondition {

  @Schema(description = "Sort direction (ASC or DESC)", example = "DESC")
  @EnumValue(
      enumClass = SortOrder.class,
      messageCode = MessageCodeConstant.MSG_CODE_204)
  private SortOrder sortOrder;
  
}
