package com.tronhanh.controller;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.listing.CreateRentalListingRequest;
import com.tronhanh.dto.request.listing.UpdateRentalListingRequest;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.listing.RentalListingDetailResponse;
import com.tronhanh.service.RentalListingService;
import com.tronhanh.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for rental listing create and update operations.
 * All endpoints require authentication. Ownership is resolved from
 * the authenticated user's security context.
 */
@Tag(
    name = "Rental Listings",
    description = "Endpoints for creating and updating rental listings")
@RestController
@RequestMapping("/api/v1/rental-listings")
@RequiredArgsConstructor
public class RentalListingController {

  private final RentalListingService rentalListingService;

  /**
   * Creates a new rental listing with status DRAFT owned by the
   * currently authenticated user.
   *
   * @param request The creation request containing listing fields
   *     and child collections.
   * @return API response wrapping the created listing detail.
   */
  @Operation(summary = "Create a new rental listing")
  @PostMapping
  public ResponseEntity<ApiResponse<RentalListingDetailResponse>>
      createListing(
          @Valid @RequestBody CreateRentalListingRequest request) {

    RentalListingDetailResponse response =
        rentalListingService.createListing(request);
    return ResponseUtils.successWithData(
        response, MessageCodeConstant.MSG_CODE_002, "Listing");
  }

  /**
   * Updates an existing rental listing using PATCH semantics. Only
   * fields present in the request body are updated. Ownership and
   * boost lock validations are enforced.
   *
   * @param listingId The UUID of the listing to update.
   * @param request The update request containing partial fields.
   * @return API response wrapping the updated listing detail.
   */
  @Operation(summary = "Update a rental listing (PATCH)")
  @PatchMapping("/{listingId}")
  public ResponseEntity<ApiResponse<RentalListingDetailResponse>>
      updateListing(
          @PathVariable UUID listingId,
          @Valid @RequestBody UpdateRentalListingRequest request) {

    RentalListingDetailResponse response =
        rentalListingService.updateListing(listingId, request);
    return ResponseUtils.successWithData(
        response, MessageCodeConstant.MSG_CODE_003, "Listing");
  }
}

