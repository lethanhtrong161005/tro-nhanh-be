package com.tronhanh.dto.request.auth;

import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for Step 2 2FA OTP code verification.
 */
@Schema(description = "Request payload for Step 2 OTP verification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest
{

  /**
   * Session ID returned from Step 1 login challenge response.
   */
  @Schema(description = "Session ID from login step 1", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
  @RequireField(field = "Session ID")
  private String sessionId;

  /**
   * 6-digit OTP verification code received by user.
   */
  @Schema(description = "6-digit OTP code received via SMS", example = "123456")
  @RequireField(field = "OTP Code")
  private String otpCode;
}
