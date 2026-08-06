package com.tronhanh.dto.request.auth;

import com.tronhanh.validation.I18nField;
import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for issuing a new Access Token via Refresh Token.
 */
@Schema(description = "Request payload for refreshing Access Token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest
{

  /**
   * Active Refresh Token issued during 2FA verification.
   */
  @Schema(description = "Refresh token string", example = "eyJhbGciOiJIUzI1NiJ9...")
  @RequireField(i18n = @I18nField(vi = "Refresh Token", en = "Refresh Token"))
  private String refreshToken;
}
