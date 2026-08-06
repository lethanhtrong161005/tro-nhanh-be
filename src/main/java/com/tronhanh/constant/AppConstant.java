package com.tronhanh.constant;

/**
 * Constants used across application infrastructure, MDC logging, security headers, and Redis keys.
 */
public final class AppConstant
{

  private AppConstant() {
    // Utility class
  }

  public static final String DEFAULT_TIMEZONE = "UTC";
  public static final String APP_PROFILE_KEY = "APP_PROFILE";
  public static final String PROFILE_LOCAL = "local";
  public static final String PROFILE_DEV = "dev";
  public static final String PROFILE_PROD = "prod";

  public static final String TRACE_ID_KEY = "traceId";
  public static final String TRACE_ID_HEADER = "X-Trace-Id";
  public static final String DEFAULT_LOCALE_HEADER = "Accept-Language";

  /**
   * Public endpoints permitted without authentication.
   */
  public static final String[] PUBLIC_ENDPOINTS = {
      "/api/v1/auth/login",
      "/api/v1/auth/verify-otp",
      "/api/v1/auth/refresh-token",
      "/api/v1/health",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v3/api-docs/**"
  };

  /**
   * Redis key prefix for OTP verification codes.
   */
  public static final String OTP_PREFIX = "otp:";

  /**
   * Redis key prefix for pending 2FA authentication sessions.
   */
  public static final String PENDING_AUTH_PREFIX = "pending_auth:";

  /**
   * Redis key prefix for active Token Family Refresh Tokens.
   */
  public static final String RT_FAMILY_PREFIX = "rt:family:";

  /**
   * Redis key prefix for revoked Token Families.
   */
  public static final String RT_REVOKED_FAMILY_PREFIX = "rt:revoked_family:";

  /**
   * Redis key prefix for blacklisted JTI Access Tokens.
   */
  public static final String BLACKLIST_JTI_PREFIX = "blacklist:jti:";

  /**
   * Expiration time in minutes for 2FA OTP codes.
   */
  public static final long OTP_TTL_MINUTES = 5L;

  /**
   * Expiration time in days for Refresh Token Families.
   */
  public static final long REFRESH_FAMILY_TTL_DAYS = 7L;
}
