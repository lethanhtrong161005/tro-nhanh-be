package com.tronhanh.dto.request.auth;

import com.tronhanh.validation.I18nField;
import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for Step 1 2FA login via phone number and password.
 */
@Schema(description = "Request payload for primary phone number and password login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest
{

  /**
   * User's registered phone number.
   */
  @Schema(description = "User registered phone number", example = "0987654321")
  @RequireField(i18n = @I18nField(vi = "Số điện thoại", en = "Phone number"))
  private String phoneNumber;

  /**
   * User's plain text password.
   */
  @Schema(description = "User account password", example = "SecurePassword123!")
  @RequireField(i18n = @I18nField(vi = "Mật khẩu", en = "Password"))
  private String password;
}
