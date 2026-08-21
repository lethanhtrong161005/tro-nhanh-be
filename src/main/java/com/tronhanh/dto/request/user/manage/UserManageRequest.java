package com.tronhanh.dto.request.user.manage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO representing the search criteria used for querying and filtering users. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserManageRequest {
  /** Unique identifier of the user to filter by. */
  private String userId;

  /** Email address to filter users (supports partial match). */
  private String email;

  /** Full name of the user to filter by (supports partial match). */
  private String fullName;

  /** Phone number of the user to filter by (supports partial match). */
  private String phoneNumber;

  /** Name of the role assigned to the user. */
  private String roleName;

  /** ID of the role assigned to the user. */
  private String roleId;
}
