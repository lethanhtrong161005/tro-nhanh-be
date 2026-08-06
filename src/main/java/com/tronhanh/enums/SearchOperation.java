package com.tronhanh.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing supported search operations for JPA dynamic criteria queries.
 */
@Schema(description = "Supported criteria search operations")
public enum SearchOperation {

  /**
   * Exact match equality (WHERE field = value).
   */
  EQUAL,

  /**
   * Inequality comparison (WHERE field != value).
   */
  NOT_EQUAL,

  /**
   * Substring search (WHERE LOWER(field) LIKE %value%).
   */
  LIKE,

  /**
   * Prefix search (WHERE LOWER(field) LIKE value%).
   */
  STARTS_WITH,

  /**
   * Suffix search (WHERE LOWER(field) LIKE %value).
   */
  ENDS_WITH,

  /**
   * Greater than comparison (WHERE field > value).
   */
  GREATER_THAN,

  /**
   * Greater than or equal to comparison (WHERE field >= value).
   */
  GREATER_THAN_EQUAL,

  /**
   * Less than comparison (WHERE field < value).
   */
  LESS_THAN,

  /**
   * Less than or equal to comparison (WHERE field <= value).
   */
  LESS_THAN_EQUAL,

  /**
   * Collection membership check (WHERE field IN (values)).
   */
  IN,

  /**
   * Collection non-membership check (WHERE field NOT IN (values)).
   */
  NOT_IN,

  /**
   * Nullability check (WHERE field IS NULL).
   */
  IS_NULL,

  /**
   * Non-nullability check (WHERE field IS NOT NULL).
   */
  IS_NOT_NULL
}
