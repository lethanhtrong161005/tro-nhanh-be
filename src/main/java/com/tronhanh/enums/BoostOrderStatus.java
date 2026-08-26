package com.tronhanh.enums;

/**
 * Represents the lifecycle status of a boost order. Flow: PENDING -> PROCESSING -> COMPLETED /
 * FAILED / EXPIRED. COMPLETED -> REFUNDED.
 */
public enum BoostOrderStatus {
  PENDING,
  PROCESSING,
  COMPLETED,
  FAILED,
  EXPIRED,
  REFUNDED
}
