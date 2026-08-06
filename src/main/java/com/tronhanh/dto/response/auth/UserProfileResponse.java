package com.tronhanh.dto.response.auth;

import com.tronhanh.enums.RoleName;
import com.tronhanh.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload representing profile information of the currently authenticated user.
 */
@Schema(description = "Response payload containing profile details of authenticated user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse
{

  /**
   * Unique user account identifier.
   */
  @Schema(description = "User unique UUID", example = "b1c2d3e4-f5a6-7b8c-9d0e-1f2a3b4c5d6e")
  private UUID userId;

  /**
   * User's primary email address.
   */
  @Schema(description = "User email address", example = "user@tronhanh.vn")
  private String email;

  /**
   * User's first name.
   */
  @Schema(description = "User first name", example = "Trong")
  private String firstName;

  /**
   * User's last name.
   */
  @Schema(description = "User last name", example = "Le")
  private String lastName;

  /**
   * User's display full name.
   */
  @Schema(description = "User display full name", example = "Le Thanh Trong")
  private String fullName;

  /**
   * User's registered phone number.
   */
  @Schema(description = "User phone number", example = "0987654321")
  private String phoneNumber;

  /**
   * User account status.
   */
  @Schema(description = "User account active status", example = "ACTIVE")
  private UserStatus status;

  /**
   * Assigned system role name.
   */
  @Schema(description = "User system role name", example = "USER")
  private RoleName roleName;
}
