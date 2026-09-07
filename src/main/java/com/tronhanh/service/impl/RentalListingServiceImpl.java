package com.tronhanh.service.impl;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.listing.RentalListingSearchRequest;
import com.tronhanh.dto.request.listing.RentalListingSortCondition;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.listing.RentalListingResponse;
import com.tronhanh.dto.specification.RentalListingSpecification;
import com.tronhanh.entity.RentalListingEntity;
import com.tronhanh.entity.RentalListingTypeEntity;
import com.tronhanh.enums.SortOrder;
import com.tronhanh.exception.HttpException;
import com.tronhanh.helper.SearchHelper;
import com.tronhanh.repository.RentalListingRepository;
import com.tronhanh.service.RentalListingService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Default service implementation for paginated rental listing retrieval. */
@Service
@RequiredArgsConstructor
public class RentalListingServiceImpl implements RentalListingService {

  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MAX_PAGE_SIZE = 100;

  private final RentalListingRepository rentalListingRepository;
  private final SearchHelper searchHelper;

  /**
   * Retrieves a bounded page of listings matching all supplied dynamic criteria.
   *
   * @param request Search and pagination parameters.
   * @return Paginated rental listing summaries.
   * @throws HttpException if a price range, page setting, or sort field is invalid.
   */
  @Override
  @Transactional(readOnly = true)
  public PageResponse<RentalListingResponse> getAllListings(RentalListingSearchRequest request) {
    RentalListingSearchRequest effectiveRequest =
        Objects.nonNull(request) ? request : new RentalListingSearchRequest();

    // Sanitize all textual query values with the established common search helper.
    searchHelper.sanitizeSearchPayload(effectiveRequest);

    // Reject invalid bounds before constructing and executing the database query.
    validateRequest(effectiveRequest);
    Pageable pageable = buildPageable(effectiveRequest);

    // Execute a database-level paginated query instead of loading the complete result set.
    Page<RentalListingEntity> listingPage =
        rentalListingRepository.findAll(
            RentalListingSpecification.build(effectiveRequest), pageable);

    // Map only the current page; listing type was loaded through the repository entity graph.
    List<RentalListingResponse> listingResponses =
        listingPage.getContent().stream().map(this::toResponse).toList();

    return PageResponse.<RentalListingResponse>builder()
        .content(listingResponses)
        .pageNumber(listingPage.getNumber())
        .pageSize(listingPage.getSize())
        .totalElements(listingPage.getTotalElements())
        .totalPages(listingPage.getTotalPages())
        .last(listingPage.isLast())
        .build();
  }

  private void validateRequest(RentalListingSearchRequest request) {
    validateRange(request.getMinPrice(), request.getMaxPrice(), "Price range");

    if (Objects.nonNull(request.getPageNumber()) && request.getPageNumber() < DEFAULT_PAGE_NUMBER) {
      throw new HttpException(HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_208);
    }
    if (Objects.nonNull(request.getPageSize())
        && (request.getPageSize() < 1 || request.getPageSize() > MAX_PAGE_SIZE)) {
      throw new HttpException(
          HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_204, "Page size");
    }
  }

  private void validateRange(BigDecimal minimum, BigDecimal maximum, String fieldName) {
    if (Objects.nonNull(minimum)
        && Objects.nonNull(maximum)
        && minimum.compareTo(maximum) > 0) {
      throw new HttpException(HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_204, fieldName);
    }
  }

  private Pageable buildPageable(RentalListingSearchRequest request) {
    int pageNumber =
        Objects.nonNull(request.getPageNumber()) ? request.getPageNumber() : DEFAULT_PAGE_NUMBER;
    int pageSize =
        Objects.nonNull(request.getPageSize()) ? request.getPageSize() : DEFAULT_PAGE_SIZE;
    Sort sort = buildSort(request.getSortBy(), request.getSortOrder());
    return PageRequest.of(pageNumber - 1, pageSize, sort);
  }

  private Sort buildSort(String sortBy, SortOrder sortOrder) {
    RentalListingSortCondition condition;
    try {
      condition =
          RentalListingSortCondition.valueOf(
              Objects.nonNull(sortBy) && !sortBy.isBlank() ? sortBy : "createdAt");
    } catch (IllegalArgumentException exception) {
      throw new HttpException(HttpStatus.BAD_REQUEST, MessageCodeConstant.MSG_CODE_205);
    }

    Sort.Direction direction =
        SortOrder.ASC.equals(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return Sort.by(direction, condition.getEntityField())
        .and(Sort.by(Sort.Direction.ASC, "listingId"));
  }

  private RentalListingResponse toResponse(RentalListingEntity listing) {
    RentalListingTypeEntity listingType = listing.getType();
    return RentalListingResponse.builder()
        .listingId(listing.getListingId())
        .title(listing.getTitle())
        .location(listing.getLocation())
        .address(listing.getAddress())
        .area(listing.getArea())
        .price(listing.getPrice())
        .status(listing.getStatus())
        .isBoosted(listing.getIsBoosted())
        .typeId(Objects.nonNull(listingType) ? listingType.getTypeId() : null)
        .typeName(Objects.nonNull(listingType) ? listingType.getName() : null)
        .createdAt(listing.getCreatedAt())
        .build();
  }
}
