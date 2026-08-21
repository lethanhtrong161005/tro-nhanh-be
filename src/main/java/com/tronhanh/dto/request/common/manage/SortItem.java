package com.tronhanh.dto.request.common.manage;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.enums.SortOrder;
import com.tronhanh.validation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base representation of sorting parameters for API requests. Contains the direction of the sort
 * (e.g., ASC, DESC).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SortItem {
  /** The order of sorting, matching the values defined in {@link com.tronhanh.enums.SortOrder}. */
  @EnumValue(enumClass = SortOrder.class, messageCode = MessageCodeConstant.MSG_CODE_206)
  private String sortOrder;
}
