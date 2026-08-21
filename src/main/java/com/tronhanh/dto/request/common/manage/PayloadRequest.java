package com.tronhanh.dto.request.common.manage;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A generic wrapper for API requests that include pagination, sorting, and specific search
 * criteria.
 *
 * @param <T> The type of the search criteria payload.
 * @param <S> The type of the sorting criteria, extending {@link SortItem}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadRequest<T, S extends SortItem> {

  /** Pagination parameters containing page size and index. */
  @Valid
  private PageItem page;

  /** Sorting parameters containing field and order details. */
  @Valid
  private S sort;

  /** Search criteria specific to the requested operation. */
  @Valid
  private T search;
}
