package com.tronhanh.constant;

/**
 * Centralized registry of application message codes mapped to message bundle keys. All constants
 * use self-referencing values (e.g. MSG_CODE_001 = "MSG_CODE_001").
 */
public final class MessageCodeConstant {

  private MessageCodeConstant() {
    // Utility class
  }

  /**
   * <ul>
   *   <li>EN: Operation successful.
   * </ul>
   */
  public static final String MSG_CODE_001 = "MSG_CODE_001";

  /**
   * <ul>
   *   <li>EN: {0} created successfully.
   * </ul>
   */
  public static final String MSG_CODE_002 = "MSG_CODE_002";

  /**
   * <ul>
   *   <li>EN: {0} updated successfully.
   * </ul>
   */
  public static final String MSG_CODE_003 = "MSG_CODE_003";

  /**
   * <ul>
   *   <li>EN: {0} deleted successfully.
   * </ul>
   */
  public static final String MSG_CODE_004 = "MSG_CODE_004";

  /**
   * <ul>
   *   <li>EN: Invalid request.
   * </ul>
   */
  public static final String MSG_CODE_100 = "MSG_CODE_100";

  /**
   * <ul>
   *   <li>EN: Unauthorized access.
   * </ul>
   */
  public static final String MSG_CODE_101 = "MSG_CODE_101";

  /**
   * <ul>
   *   <li>EN: Access denied.
   * </ul>
   */
  public static final String MSG_CODE_102 = "MSG_CODE_102";

  /**
   * <ul>
   *   <li>EN: {0} not found.
   * </ul>
   */
  public static final String MSG_CODE_103 = "MSG_CODE_103";

  /**
   * <ul>
   *   <li>EN: {0} already exists.
   * </ul>
   */
  public static final String MSG_CODE_104 = "MSG_CODE_104";

  /**
   * <ul>
   *   <li>EN: Internal server error.
   * </ul>
   */
  public static final String MSG_CODE_105 = "MSG_CODE_105";

  /**
   * <ul>
   *   <li>EN: {0} is required.
   * </ul>
   */
  public static final String MSG_CODE_200 = "MSG_CODE_200";

  /**
   * <ul>
   *   <li>EN: Invalid or expired token.
   * </ul>
   */
  public static final String MSG_CODE_201 = "MSG_CODE_201";

  /**
   * <ul>
   *   <li>EN: Missing authentication token.
   * </ul>
   */
  public static final String MSG_CODE_202 = "MSG_CODE_202";

  /**
   * <ul>
   *   <li>EN: Token has been blacklisted.
   * </ul>
   */
  public static final String MSG_CODE_203 = "MSG_CODE_203";

  /**
   * <ul>
   *   <li>EN: {0} is invalid.
   * </ul>
   */
  public static final String MSG_CODE_204 = "MSG_CODE_204";

  /**
   * <ul>
   *   <li>EN: Invalid sort field.
   * </ul>
   */
  public static final String MSG_CODE_205 = "MSG_CODE_205";

  /**
   * <ul>
   *   <li>EN: Sort order must be ASC or DESC.
   * </ul>
   */
  public static final String MSG_CODE_206 = "MSG_CODE_206";

  /**
   * <ul>
   *   <li>EN: Page number is required.
   * </ul>
   */
  public static final String MSG_CODE_207 = "MSG_CODE_207";

  /**
   * <ul>
   *   <li>EN: Page number must be >= 1.
   * </ul>
   */
  public static final String MSG_CODE_208 = "MSG_CODE_208";

  /**
   * <ul>
   *   <li>EN: Page size is required.
   * </ul>
   */
  public static final String MSG_CODE_209 = "MSG_CODE_209";

  /**
   * <ul>
   *   <li>EN: Page size must be >= 1.
   * </ul>
   */
  public static final String MSG_CODE_210 = "MSG_CODE_210";

  /**
   * <ul>
   *   <li>EN: Please wait before requesting a new OTP.
   * </ul>
   */
  public static final String MSG_CODE_211 = "MSG_CODE_211";

  /**
   * <ul>
   *   <li>EN: {0} has expired.
   * </ul>
   */
  public static final String MSG_CODE_212 = "MSG_CODE_212";
}
