package com.tronhanh.service;

import com.tronhanh.dto.request.listing.CreateRentalListingRequest;
import com.tronhanh.dto.request.listing.UpdateRentalListingRequest;
import com.tronhanh.dto.response.listing.RentalListingDetailResponse;
import java.util.UUID;

/**
 * Service interface for rental listing create and update operations.
 */
public interface RentalListingService {

  /**
   * Creates a new rental listing owned by the currently authenticated
   * user.
   *
   * <p>Step-by-step flow:
   *
   * <ol>
   *   <li>Extract current user from SecurityContext as owner.
   *   <li>Validate listing type exists and is active.
   *   <li>Validate selected amenities exist (if provided).
   *   <li>Build listing entity with status DRAFT.
   *   <li>Build and attach child entities (images, amenities,
   *       amenity locations, cost-of-living).
   *   <li>Persist within a single transaction.
   *   <li>Return hydrated detail response.
   * </ol>
   *
   * @param request The creation request containing listing fields
   *     and child collections.
   * @return RentalListingDetailResponse with the created listing.
   * @throws com.tronhanh.exception.HttpException If listing type
   *     not found (404).
   * @throws com.tronhanh.exception.HttpException If amenities not
   *     found (400).
   */
  RentalListingDetailResponse createListing(
      CreateRentalListingRequest request);

  /**
   * Updates an existing rental listing using PATCH semantics.
   *
   * <p>Business rules enforced:
   *
   * <ol>
   *   <li>Verify ownership: current user must be the listing owner.
   *   <li>Check boost lock: if listing is boosted, reject edits.
   *   <li>Apply only non-null fields from the request (PATCH).
   *   <li>Replace child collections if provided in the request.
   *   <li>Auto re-moderate: if listing was ACTIVE, revert status to
   *       PENDING_APPROVAL.
   *   <li>Persist within a single transaction.
   *   <li>Return hydrated detail response.
   * </ol>
   *
   * @param listingId The UUID of the listing to update.
   * @param request The update request containing partial fields.
   * @return RentalListingDetailResponse with the updated listing.
   * @throws com.tronhanh.exception.HttpException If listing not
   *     found (404).
   * @throws com.tronhanh.exception.HttpException If not the owner
   *     (403).
   * @throws com.tronhanh.exception.HttpException If listing is
   *     boosted (409).
   */
  RentalListingDetailResponse updateListing(
      UUID listingId, UpdateRentalListingRequest request);
}

