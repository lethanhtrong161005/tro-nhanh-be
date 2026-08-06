package com.tronhanh.constant;

/**
 * Centralized registry of application message codes mapped to i18n bundle keys.
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
   * <ul>
   *   <li>VI: Thao tác thành công.
   *   <li>EN: Operation successful.
   * </ul>
   */
  public static final String MSG_CODE_001 = "MSG_CODE_001";

  /**
   * <ul>
   *   <li>VI: {0} tạo thành công.
   *   <li>EN: {0} created successfully.
   * </ul>
   */
  public static final String MSG_CODE_002 = "MSG_CODE_002";

  /**
   * <ul>
   *   <li>VI: {0} cập nhật thành công.
   *   <li>EN: {0} updated successfully.
   * </ul>
   */
  public static final String MSG_CODE_003 = "MSG_CODE_003";

  /**
   * <ul>
   *   <li>VI: {0} xóa thành công.
   *   <li>EN: {0} deleted successfully.
   * </ul>
   */
  public static final String MSG_CODE_004 = "MSG_CODE_004";

  // ============================================================
  // Client Error Messages (100 - 199)
  // ============================================================

  /**
   * <ul>
   *   <li>VI: Yêu cầu không hợp lệ.
   *   <li>EN: Invalid request.
   * </ul>
   */
  public static final String MSG_CODE_100 = "MSG_CODE_100";

  /**
   * <ul>
   *   <li>VI: Yêu cầu xác thực.
   *   <li>EN: Unauthorized access.
   * </ul>
   */
  public static final String MSG_CODE_101 = "MSG_CODE_101";

  /**
   * <ul>
   *   <li>VI: Không có quyền truy cập.
   *   <li>EN: Access denied.
   * </ul>
   */
  public static final String MSG_CODE_102 = "MSG_CODE_102";

  /**
   * <ul>
   *   <li>VI: {0} không tìm thấy.
   *   <li>EN: {0} not found.
   * </ul>
   */
  public static final String MSG_CODE_103 = "MSG_CODE_103";

  /**
   * <ul>
   *   <li>VI: {0} đã tồn tại.
   *   <li>EN: {0} already exists.
   * </ul>
   */
  public static final String MSG_CODE_104 = "MSG_CODE_104";

  /**
   * <ul>
   *   <li>VI: Lỗi hệ thống nội bộ.
   *   <li>EN: Internal server error.
   * </ul>
   */
  public static final String MSG_CODE_105 = "MSG_CODE_105";

  // ============================================================
  // Validation Messages (200 - 299)
  // ============================================================

  /**
   * <ul>
   *   <li>VI: {0} là bắt buộc.
   *   <li>EN: {0} is required.
   * </ul>
   */
  public static final String MSG_CODE_200 = "MSG_CODE_200";

  /**
   * <ul>
   *   <li>VI: Token không hợp lệ hoặc đã hết hạn.
   *   <li>EN: Invalid or expired token.
   * </ul>
   */
  public static final String MSG_CODE_201 = "MSG_CODE_201";

  /**
   * <ul>
   *   <li>VI: Không tìm thấy token xác thực.
   *   <li>EN: Missing authentication token.
   * </ul>
   */
  public static final String MSG_CODE_202 = "MSG_CODE_202";

  /**
   * <ul>
   *   <li>VI: Token đã bị vô hiệu hóa.
   *   <li>EN: Token has been blacklisted.
   * </ul>
   */
  public static final String MSG_CODE_203 = "MSG_CODE_203";

  /**
   * <ul>
   *   <li>VI: {0} không hợp lệ.
   *   <li>EN: {0} is invalid.
   * </ul>
   */
  public static final String MSG_CODE_204 = "MSG_CODE_204";

  /**
   * <ul>
   *   <li>VI: Trường sắp xếp không hợp lệ.
   *   <li>EN: Invalid sort field.
   * </ul>
   */
  public static final String MSG_CODE_205 = "MSG_CODE_205";

  /**
   * <ul>
   *   <li>VI: Chiều sắp xếp phải là ASC hoặc DESC.
   *   <li>EN: Sort order must be ASC or DESC.
   * </ul>
   */
  public static final String MSG_CODE_206 = "MSG_CODE_206";

  /**
   * <ul>
   *   <li>VI: Số trang không được để trống.
   *   <li>EN: Page number is required.
   * </ul>
   */
  public static final String MSG_CODE_207 = "MSG_CODE_207";

  /**
   * <ul>
   *   <li>VI: Số trang phải lớn hơn hoặc bằng 1.
   *   <li>EN: Page number must be >= 1.
   * </ul>
   */
  public static final String MSG_CODE_208 = "MSG_CODE_208";

  /**
   * <ul>
   *   <li>VI: Kích thước trang không được để trống.
   *   <li>EN: Page size is required.
   * </ul>
   */
  public static final String MSG_CODE_209 = "MSG_CODE_209";

  /**
   * <ul>
   *   <li>VI: Kích thước trang phải lớn hơn hoặc bằng 1.
   *   <li>EN: Page size must be >= 1.
   * </ul>
   */
  public static final String MSG_CODE_210 = "MSG_CODE_210";
}
