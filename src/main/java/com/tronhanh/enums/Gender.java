package com.tronhanh.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enumeration representing user gender options.
 */
@Schema(description = "User gender (MALE, FEMALE, OTHER)")
public enum Gender
{
  MALE,
  FEMALE,
  OTHER
}