package com.tronhanh.dto.response.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload for Step 1 2FA login challenge containing the session ID for OTP verification.
 */
@Schema(description = "Response payload for Step 1 2FA login OTP challenge")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse
{

  /**
   * Unique session ID associated with the pending 2FA OTP verification.
   */
  @Schema(description = "Session identifier for OTP verification step", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
  private String sessionId;

  /**
   * Information message indicating OTP transmission status.
   */
  @Schema(description = "Localized result message", example = "Thao tác thành công.")
  private String message;

  /**
   * Expiration time in seconds for the OTP challenge session.
   */
  @Schema(description = "OTP session validity period in seconds", example = "300")
  private long expiresInSeconds;
}
