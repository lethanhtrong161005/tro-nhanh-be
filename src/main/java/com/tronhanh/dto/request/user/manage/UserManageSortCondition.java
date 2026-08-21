package com.tronhanh.dto.request.user.manage;

import com.tronhanh.enums.SortOrder;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.domain.Sort;

/**
 * Enum defining the allowable sorting fields for User Management. Maps API sort keys to their
 * corresponding database entity fields.
 */
@Getter
public enum UserManageSortCondition {
  /** Sort by User ID. */
  userId("userId", "id"),
  /** Sort by Email. */
  email("email", "email"),
  /** Sort by Full Name. */
  fullName("fullName", "fullName"),
  /** Sort by Phone Number. */
  phoneNumber("phoneNumber", "phoneNumber"),
  /** Sort by Last Update timestamp. */
  updatedAt("updatedAt", "updatedAt"),
  /** Sort by associated Role ID. */
  roleId("roleId", "role.id");

  /** The field name exposed and received via the API. */
  private final String apiField;

  /** The actual field name mapped in the database entity for sorting. */
  private final String dbField;

  UserManageSortCondition(String apiField, String dbField) {
    this.apiField = apiField;
    this.dbField = dbField;
  }

  /**
   * Constructs a Spring Data {@link Sort} object based on the given sort request. Applies fallback
   * defaults (updatedAt DESC) if inputs are invalid or missing.
   *
   * @param sortRequest The sort parameters received from the client.
   * @return A valid Spring Data Sort object.
   */
  public static Sort buildSort(UserSortRequest sortRequest) {
    if (Objects.isNull(sortRequest)
        || Objects.isNull(sortRequest.getSortBy())
        || sortRequest.getSortBy().isBlank()) {
      return Sort.by(Sort.Direction.DESC, UserManageSortCondition.updatedAt.getDbField());
    }

    String dbField;
    try {
      dbField = UserManageSortCondition.valueOf(sortRequest.getSortBy()).getDbField();
    } catch (IllegalArgumentException e) {
      return Sort.by(Sort.Direction.DESC, UserManageSortCondition.updatedAt.getDbField());
    }

    Sort.Direction direction = Sort.Direction.ASC;
    if (SortOrder.DESC.name().equalsIgnoreCase(sortRequest.getSortOrder())) {
      direction = Sort.Direction.DESC;
    }

    return Sort.by(direction, dbField);
  }
}
