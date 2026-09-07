package com.tronhanh.service.impl;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.listing.CreateRentalListingRequest;
import com.tronhanh.dto.request.listing.UpdateRentalListingRequest;
import com.tronhanh.dto.response.listing.RentalListingDetailResponse;
import com.tronhanh.entity.AmenityEntity;
import com.tronhanh.entity.RentalListingEntity;
import com.tronhanh.entity.RentalListingTypeEntity;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.enums.RentalListingStatus;
import com.tronhanh.exception.HttpException;
import com.tronhanh.helper.RentalListingHelper;
import com.tronhanh.repository.AmenityRepository;
import com.tronhanh.repository.RentalListingRepository;
import com.tronhanh.repository.RentalListingTypeRepository;
import com.tronhanh.service.RentalListingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link RentalListingService} providing create and
 * update operations for rental listings with full business rule
 * enforcement.
 *
 * <p>Thread-safe: Yes (stateless Spring singleton)
 *
 * @see RentalListingHelper
 */
@Service
@RequiredArgsConstructor
public class RentalListingServiceImpl implements RentalListingService {

  private static final Logger logger =
      LoggerFactory.getLogger(RentalListingServiceImpl.class);

  private final RentalListingRepository rentalListingRepository;
  private final RentalListingTypeRepository listingTypeRepository;
  private final AmenityRepository amenityRepository;
  private final RentalListingHelper rentalListingHelper;

  @Override
  @Transactional
  public RentalListingDetailResponse createListing(
      CreateRentalListingRequest request) {

    // 1. Extract current authenticated user as the listing owner
    UserEntity currentUser = getCurrentUser();
    logger.info("Creating listing for user: {}", currentUser.getUserId());

    // 2. Validate that the listing type exists and is active
    RentalListingTypeEntity listingType =
        listingTypeRepository
            .findByTypeIdAndIsDeletedFalse(request.getTypeId())
            .orElseThrow(() -> new HttpException(
                HttpStatus.NOT_FOUND,
                MessageCodeConstant.MSG_CODE_302));

    // 3. Validate selected amenities exist (if provided)
    List<AmenityEntity> amenities = resolveAmenities(
        request.getAmenityIds());

    // 4. Build the core listing entity with status = DRAFT
    RentalListingEntity listing =
        rentalListingHelper.buildListingEntity(
            request, currentUser, listingType);

    // 5. Build and attach child entities within the same transaction
    List<com.tronhanh.entity.RentalListingImageEntity> images =
        rentalListingHelper.buildImageEntities(
            request.getImages(), listing);
    listing.getImages().addAll(images);

    List<com.tronhanh.entity.RentalListingAmenityEntity>
        amenityEntities =
            rentalListingHelper.buildAmenityEntities(
                amenities, listing);
    listing.getListingAmenities().addAll(amenityEntities);

    List<com.tronhanh.entity.RentalListingAmenityLocEntity>
        amenityLocs =
            rentalListingHelper.buildAmenityLocEntities(
                request.getAmenityLocations(), listing);
    listing.getAmenityLocations().addAll(amenityLocs);

    // Build cost-of-living (1:1 relationship)
    com.tronhanh.entity.RentalListingCOLEntity costOfLiving =
        rentalListingHelper.buildCostOfLivingEntity(
            request.getCostOfLiving(), listing);
    listing.setCostOfLiving(costOfLiving);

    // 6. Persist the listing with all children via cascade
    RentalListingEntity savedListing =
        rentalListingRepository.save(listing);
    logger.info("Listing created: {}", savedListing.getListingId());

    // 7. Map and return the hydrated detail response
    return rentalListingHelper.mapToDetailResponse(savedListing);
  }

  @Override
  @Transactional
  public RentalListingDetailResponse updateListing(
      UUID listingId, UpdateRentalListingRequest request) {

    // 1. Fetch listing and verify it exists and is not soft-deleted
    RentalListingEntity listing =
        rentalListingRepository
            .findByListingIdAndIsDeletedFalse(listingId)
            .orElseThrow(() -> new HttpException(
                HttpStatus.NOT_FOUND,
                MessageCodeConstant.MSG_CODE_103,
                "Listing"));

    // 2. Verify ownership: currentUser must be the listing owner
    UserEntity currentUser = getCurrentUser();
    if (!listing.getOwner().getUserId()
        .equals(currentUser.getUserId())) {
      throw new HttpException(
          HttpStatus.FORBIDDEN,
          MessageCodeConstant.MSG_CODE_301);
    }

    // 3. Check boost lock: no edits when listing is actively boosted
    if (Boolean.TRUE.equals(listing.getIsBoosted())) {
      throw new HttpException(
          HttpStatus.CONFLICT,
          MessageCodeConstant.MSG_CODE_300);
    }

    // 4. Resolve listing type if typeId is being changed
    RentalListingTypeEntity newType = null;
    if (Objects.nonNull(request.getTypeId())) {
      newType = listingTypeRepository
          .findByTypeIdAndIsDeletedFalse(request.getTypeId())
          .orElseThrow(() -> new HttpException(
              HttpStatus.NOT_FOUND,
              MessageCodeConstant.MSG_CODE_302));
    }

    // 5. Apply PATCH: update only non-null scalar fields
    rentalListingHelper.applyPatchFields(
        request, listing, newType);

    // 6. Replace child collections if provided in the request
    if (Objects.nonNull(request.getImages())) {
      rentalListingHelper.replaceImages(
          request.getImages(), listing);
    }

    if (Objects.nonNull(request.getAmenityIds())) {
      List<AmenityEntity> amenities =
          resolveAmenities(request.getAmenityIds());
      rentalListingHelper.replaceAmenities(amenities, listing);
    }

    if (Objects.nonNull(request.getAmenityLocations())) {
      rentalListingHelper.replaceAmenityLocations(
          request.getAmenityLocations(), listing);
    }

    if (Objects.nonNull(request.getCostOfLiving())) {
      rentalListingHelper.replaceCostOfLiving(
          request.getCostOfLiving(), listing);
    }

    // 7. Auto re-moderate: ACTIVE listing edits revert to
    //    PENDING_APPROVAL for admin re-verification
    if (RentalListingStatus.ACTIVE.equals(listing.getStatus())) {
      listing.setStatus(RentalListingStatus.PENDING_APPROVAL);
      logger.info(
          "Listing {} reverted to PENDING_APPROVAL after edit",
          listingId);
    }

    // 8. Persist the updated listing (cascade handles children)
    RentalListingEntity savedListing =
        rentalListingRepository.save(listing);
    logger.info("Listing updated: {}", savedListing.getListingId());

    // 9. Map and return the hydrated detail response
    return rentalListingHelper.mapToDetailResponse(savedListing);
  }

  // ---------------------------------------------------------------
  // Private helper methods
  // ---------------------------------------------------------------

  /**
   * Extracts the current authenticated user from SecurityContext.
   *
   * @return The authenticated UserEntity.
   * @throws HttpException If user is not authenticated (401).
   */
  private UserEntity getCurrentUser() {
    Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication)
        || !(authentication.getPrincipal()
            instanceof UserEntity user)) {
      throw new HttpException(
          HttpStatus.UNAUTHORIZED,
          MessageCodeConstant.MSG_CODE_101);
    }
    return user;
  }

  /**
   * Resolves amenity entities from a list of amenity IDs. Validates
   * that all requested amenities exist in the system.
   *
   * @param amenityIds List of amenity UUIDs to resolve.
   * @return List of resolved amenity entities (empty if input is
   *     null/empty).
   * @throws HttpException If any amenity IDs are not found (400).
   */
  private List<AmenityEntity> resolveAmenities(
      List<UUID> amenityIds) {

    if (Objects.isNull(amenityIds) || amenityIds.isEmpty()) {
      return new ArrayList<>();
    }

    // Query all active amenities matching the provided IDs
    List<AmenityEntity> amenities =
        amenityRepository.findAllByAmenityIdInAndIsDeletedFalse(
            amenityIds);

    // Verify all requested amenities were found
    if (amenities.size() != amenityIds.size()) {
      throw new HttpException(
          HttpStatus.BAD_REQUEST,
          MessageCodeConstant.MSG_CODE_303);
    }

    return amenities;
  }
}

