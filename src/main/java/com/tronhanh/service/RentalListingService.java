package com.tronhanh.service;

import com.tronhanh.dto.request.listing.RentalListingSearchRequest;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.listing.RentalListingResponse;

/** Service contract for retrieving rental listings. */
public interface RentalListingService {

  /**
   * Retrieves rental listings using dynamic search, filtering, sorting, and pagination.
   *
   * @param request Search and pagination parameters.
   * @return Paginated rental listing summaries.
   */
  PageResponse<RentalListingResponse> getAllListings(RentalListingSearchRequest request);
}
