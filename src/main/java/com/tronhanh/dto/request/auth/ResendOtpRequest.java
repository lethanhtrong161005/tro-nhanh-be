package com.tronhanh.dto.request.auth;

import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for resending a fresh 6-digit OTP code during an active 2FA login session.
 *
 * <p>The session must still be valid in Redis. A cooldown is enforced between consecutive resend
 * requests to prevent SMS abuse.
 */
@Schema(description = "Request payload to resend OTP code for an active 2FA session.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResendOtpRequest {

  /**
   * Active session ID returned from the POST /login endpoint. The session must not have expired
   * (default TTL: 5 minutes).
   */
  @Schema(
      description = "Active session ID returned from the /login endpoint.",
      example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
  @RequireField(field = "Session ID")
  private String sessionId;
}
