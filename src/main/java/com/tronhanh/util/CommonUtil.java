package com.tronhanh.util;

import com.github.f4b6a3.uuid.UuidCreator;
import java.security.SecureRandom;
import java.util.UUID;

/** Utility class providing common helper methods across the application. */
public final class CommonUtil {

  private CommonUtil() {
    // Utility class
  }

  /**
   * Generates a random 6-digit numeric OTP code.
   *
   * @return 6-digit string representation of OTP code.
   */
  public static String generateOtpCode() {
    SecureRandom random = new SecureRandom();
    int code = 100000 + random.nextInt(900000);
    return String.valueOf(code);
  }

  /**
   * Generates a time-ordered UUIDv7 based on the Unix Epoch. Naturally sortable by creation time,
   * making it index-friendly.
   *
   * @return A newly generated UUIDv7.
   */
  public static UUID generateUuidV7() {
    return UuidCreator.getTimeOrderedEpoch();
  }
}
