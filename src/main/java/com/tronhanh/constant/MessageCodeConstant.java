package com.tronhanh.constant;

/**
 * Centralized registry of application message codes mapped to message bundle keys.
 * All constants use self-referencing values (e.g. MSG_CODE_001 = "MSG_CODE_001").
 */
public final class MessageCodeConstant {

  private MessageCodeConstant() {
    // Utility class
  }

  // ============================================================
  // Success Messages (001 - 099)
  // ============================================================

  /**
   * Operation successful.
   */
  public static final String MSG_CODE_001 = "MSG_CODE_001";

  /**
   * {0} created successfully.
   */
  public static final String MSG_CODE_002 = "MSG_CODE_002";

  /**
   * {0} updated successfully.
   */
  public static final String MSG_CODE_003 = "MSG_CODE_003";

  /**
   * {0} deleted successfully.
   */
  public static final String MSG_CODE_004 = "MSG_CODE_004";

  // ============================================================
  // Client Error Messages (100 - 199)
  // ============================================================

  /**
   * Invalid request.
   */
  public static final String MSG_CODE_100 = "MSG_CODE_100";

  /**
   * Unauthorized access.
   */
  public static final String MSG_CODE_101 = "MSG_CODE_101";

  /**
   * Access denied.
   */
  public static final String MSG_CODE_102 = "MSG_CODE_102";

  /**
   * {0} not found.
   */
  public static final String MSG_CODE_103 = "MSG_CODE_103";

  /**
   * {0} already exists.
   */
  public static final String MSG_CODE_104 = "MSG_CODE_104";

  /**
   * Internal server error.
   */
  public static final String MSG_CODE_105 = "MSG_CODE_105";

  // ============================================================
  // Validation Messages (200 - 299)
  // ============================================================

  /**
   * {0} is required.
   */
  public static final String MSG_CODE_200 = "MSG_CODE_200";

  /**
   * Invalid or expired token.
   */
  public static final String MSG_CODE_201 = "MSG_CODE_201";

  /**
   * Missing authentication token.
   */
  public static final String MSG_CODE_202 = "MSG_CODE_202";

  /**
   * Token has been blacklisted.
   */
  public static final String MSG_CODE_203 = "MSG_CODE_203";

  /**
   * {0} is invalid.
   */
  public static final String MSG_CODE_204 = "MSG_CODE_204";

  /**
   * Invalid sort field.
   */
  public static final String MSG_CODE_205 = "MSG_CODE_205";

  /**
   * Sort order must be ASC or DESC.
   */
  public static final String MSG_CODE_206 = "MSG_CODE_206";

  /**
   * Page number is required.
   */
  public static final String MSG_CODE_207 = "MSG_CODE_207";

  /**
   * Page number must be >= 1.
   */
  public static final String MSG_CODE_208 = "MSG_CODE_208";

  /**
   * Page size is required.
   */
  public static final String MSG_CODE_209 = "MSG_CODE_209";

  /**
   * Page size must be >= 1.
   */
  public static final String MSG_CODE_210 = "MSG_CODE_210";
}
