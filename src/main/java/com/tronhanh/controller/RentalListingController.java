package com.tronhanh.controller;

import com.tronhanh.dto.request.listing.RentalListingSearchRequest;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.listing.RentalListingResponse;
import com.tronhanh.service.RentalListingService;
import com.tronhanh.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for rental listing retrieval operations. */
@Tag(name = "Rental Listings", description = "Endpoints for searching rental listings")
@RestController
@RequestMapping("/api/v1/rental-listings")
@RequiredArgsConstructor
public class RentalListingController {

  private final RentalListingService rentalListingService;

  /**
   * Retrieves a filtered and paginated collection of rental listings.
   *
   * @param request Optional query parameters for search, filtering, sorting, and pagination.
   * @return Standard API response containing the paginated listing summaries.
   */
  @Operation(summary = "Get all rental listings with search, filters, and pagination")
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<RentalListingResponse>>> getAllListings(
      @Valid @ModelAttribute RentalListingSearchRequest request) {
    PageResponse<RentalListingResponse> response = rentalListingService.getAllListings(request);
    return ResponseUtils.successWithData(response);
  }
}
