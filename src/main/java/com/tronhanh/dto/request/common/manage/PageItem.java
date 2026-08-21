package com.tronhanh.dto.request.common.manage;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.validation.MinValue;
import com.tronhanh.validation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Represents pagination parameters for API requests. Contains page number and page size
 * configurations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageItem {

  /** The number of items per page. Must be at least 1. */
  @RequireField(messageCode = MessageCodeConstant.MSG_CODE_209, field = "Page Size")
  @MinValue(value = 1, messageCode = MessageCodeConstant.MSG_CODE_210)
  private Integer pageSize;

  /** The requested page number. Must be at least 1. */
  @RequireField(messageCode = MessageCodeConstant.MSG_CODE_207, field = "Page Number")
  @MinValue(value = 1, messageCode = MessageCodeConstant.MSG_CODE_208)
  private Integer pageNumber;

  /**
   * Builds a Spring Data {@link org.springframework.data.domain.Pageable} object from this
   * PageItem.
   *
   * @param pageItem The pagination item payload, can be null.
   * @param sort The Spring Data Sort object to apply.
   * @return A built Pageable instance with a fallback to default page size and index if omitted.
   */
  public static Pageable buildPageable(
      PageItem pageItem, Sort sort) {
    int page =
        (pageItem != null && pageItem.getPageNumber() != null) ? pageItem.getPageNumber() : 1;
    int size = (pageItem != null && pageItem.getPageSize() != null) ? pageItem.getPageSize() : 10;
    return PageRequest.of(Math.max(0, page - 1), size, sort);
  }
}
