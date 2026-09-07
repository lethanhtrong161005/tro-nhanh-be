package com.tronhanh.dto.request.listing;

import lombok.Getter;

/** Allowlist mapping public rental listing sort keys to entity attributes. */
@Getter
public enum RentalListingSortCondition {
  createdAt("createdAt"),
  updatedAt("updatedAt"),
  price("price"),
  area("area"),
  title("title");

  private final String entityField;

  RentalListingSortCondition(String entityField) {
    this.entityField = entityField;
  }
}
