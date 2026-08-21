package com.tronhanh.dto.response.user.manage;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/** DTO representing a user's details returned in the user management API responses. */
@Data
@Builder
public class UserManageResponse {
  /** The unique identifier of the user. */
  private UUID userId;

  /** The email address of the user. */
  private String email;

  /** The full name of the user. */
  private String fullName;

  /** The phone number of the user. */
  private String phoneNumber;

  /** The current status of the user account. */
  private String status;

  /** The name of the role assigned to the user. */
  private String roleName;
}
