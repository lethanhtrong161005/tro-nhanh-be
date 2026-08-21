package com.tronhanh.dto.request.user.manage;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.common.manage.SortItem;
import com.tronhanh.validation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Specific sorting parameters for user management queries. Inherits the sort order property and
 * adds a specific field to sort by.
 */
@Schema(description = "User sort parameters")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSortRequest extends SortItem {

  @Schema(description = "Field to sort by", example = "userId")
  @EnumValue(
      enumClass = UserManageSortCondition.class,
      messageCode = MessageCodeConstant.MSG_CODE_205)
  private String sortBy;
}
