package com.tronhanh.helper;

import com.tronhanh.dto.request.listing.AmenityLocationRequest;
import com.tronhanh.dto.request.listing.CostOfLivingRequest;
import com.tronhanh.dto.request.listing.CreateRentalListingRequest;
import com.tronhanh.dto.request.listing.ImageRequest;
import com.tronhanh.dto.request.listing.UpdateRentalListingRequest;
import com.tronhanh.dto.response.listing.AmenityLocationResponse;
import com.tronhanh.dto.response.listing.AmenityResponse;
import com.tronhanh.dto.response.listing.CostOfLivingResponse;
import com.tronhanh.dto.response.listing.ImageResponse;
import com.tronhanh.dto.response.listing.ListingTypeResponse;
import com.tronhanh.dto.response.listing.RentalListingDetailResponse;
import com.tronhanh.entity.AmenityEntity;
import com.tronhanh.entity.RentalListingAmenityEntity;
import com.tronhanh.entity.RentalListingAmenityLocEntity;
import com.tronhanh.entity.RentalListingCOLEntity;
import com.tronhanh.entity.RentalListingEntity;
import com.tronhanh.entity.RentalListingImageEntity;
import com.tronhanh.entity.RentalListingTypeEntity;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.enums.RentalListingStatus;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Helper component for RentalListingService to handle entity building
 * and DTO mapping logic. Keeps the Service class focused on
 * orchestration.
 */
@Component
public class RentalListingHelper {

  /**
   * Builds a new {@link RentalListingEntity} from the create request.
   *
   * @param request The creation request DTO.
   * @param owner The authenticated owner entity.
   * @param type The resolved listing type entity.
   * @return A new listing entity with status DRAFT.
   */
  public RentalListingEntity buildListingEntity(
      CreateRentalListingRequest request,
      UserEntity owner,
      RentalListingTypeEntity type) {

    return RentalListingEntity.builder()
        .owner(owner)
        .type(type)
        .title(request.getTitle())
        .location(request.getLocation())
        .address(request.getAddress())
        .area(request.getArea())
        .price(request.getPrice())
        .phoneContact(request.getPhoneContact())
        .accessPolicy(request.getAccessPolicy())
        .accessOpenTime(request.getAccessOpenTime())
        .accessCloseTime(request.getAccessCloseTime())
        .description(request.getDescription())
        .status(RentalListingStatus.DRAFT)
        .isBoosted(false)
        .build();
  }

  /**
   * Builds image child entities from request DTOs.
   *
   * @param imageRequests List of image request DTOs.
   * @param listing The parent listing entity.
   * @return List of image entities linked to the listing.
   */
  public List<RentalListingImageEntity> buildImageEntities(
      List<ImageRequest> imageRequests,
      RentalListingEntity listing) {

    if (Objects.isNull(imageRequests) || imageRequests.isEmpty()) {
      return new ArrayList<>();
    }

    return imageRequests.stream()
        .map(req -> RentalListingImageEntity.builder()
            .listing(listing)
            .imageUrl(req.getImageUrl())
            .displayOrder(req.getDisplayOrder())
            .build())
        .collect(Collectors.toList());
  }

  /**
   * Builds amenity junction entities from amenity entities.
   *
   * @param amenities List of resolved amenity entities.
   * @param listing The parent listing entity.
   * @return List of junction entities linking listing to amenities.
   */
  public List<RentalListingAmenityEntity> buildAmenityEntities(
      List<AmenityEntity> amenities,
      RentalListingEntity listing) {

    if (Objects.isNull(amenities) || amenities.isEmpty()) {
      return new ArrayList<>();
    }

    return amenities.stream()
        .map(amenity -> RentalListingAmenityEntity.builder()
            .listing(listing)
            .amenity(amenity)
            .build())
        .collect(Collectors.toList());
  }

  /**
   * Builds amenity location child entities from request DTOs.
   *
   * @param locationRequests List of amenity location request DTOs.
   * @param listing The parent listing entity.
   * @return List of amenity location entities linked to the listing.
   */
  public List<RentalListingAmenityLocEntity> buildAmenityLocEntities(
      List<AmenityLocationRequest> locationRequests,
      RentalListingEntity listing) {

    if (Objects.isNull(locationRequests) || locationRequests.isEmpty()) {
      return new ArrayList<>();
    }

    return locationRequests.stream()
        .map(req -> RentalListingAmenityLocEntity.builder()
            .listing(listing)
            .type(req.getType())
            .description(req.getDescription())
            .distance(req.getDistance())
            .build())
        .collect(Collectors.toList());
  }

  /**
   * Builds a cost-of-living child entity from request DTO.
   *
   * @param colRequest The cost-of-living request DTO.
   * @param listing The parent listing entity.
   * @return The cost-of-living entity or null if request is null.
   */
  public RentalListingCOLEntity buildCostOfLivingEntity(
      CostOfLivingRequest colRequest,
      RentalListingEntity listing) {

    if (Objects.isNull(colRequest)) {
      return null;
    }

    return RentalListingCOLEntity.builder()
        .listing(listing)
        .electricityBill(colRequest.getElectricityBill())
        .waterBill(colRequest.getWaterBill())
        .servicesFee(colRequest.getServicesFee())
        .deposit(colRequest.getDeposit())
        .build();
  }

  /**
   * Applies PATCH fields from the update request to the existing
   * listing entity. Only non-null fields are updated.
   *
   * @param request The update request containing partial fields.
   * @param listing The existing listing entity to patch.
   * @param type The resolved listing type (null if typeId not in
   *     request).
   */
  public void applyPatchFields(
      UpdateRentalListingRequest request,
      RentalListingEntity listing,
      RentalListingTypeEntity type) {

    // Apply type if provided
    if (Objects.nonNull(type)) {
      listing.setType(type);
    }
    if (Objects.nonNull(request.getTitle())) {
      listing.setTitle(request.getTitle());
    }
    if (Objects.nonNull(request.getLocation())) {
      listing.setLocation(request.getLocation());
    }
    if (Objects.nonNull(request.getAddress())) {
      listing.setAddress(request.getAddress());
    }
    if (Objects.nonNull(request.getArea())) {
      listing.setArea(request.getArea());
    }
    if (Objects.nonNull(request.getPrice())) {
      listing.setPrice(request.getPrice());
    }
    if (Objects.nonNull(request.getPhoneContact())) {
      listing.setPhoneContact(request.getPhoneContact());
    }
    if (Objects.nonNull(request.getAccessPolicy())) {
      listing.setAccessPolicy(request.getAccessPolicy());
    }
    if (Objects.nonNull(request.getAccessOpenTime())) {
      listing.setAccessOpenTime(request.getAccessOpenTime());
    }
    if (Objects.nonNull(request.getAccessCloseTime())) {
      listing.setAccessCloseTime(request.getAccessCloseTime());
    }
    if (Objects.nonNull(request.getDescription())) {
      listing.setDescription(request.getDescription());
    }
  }

  /**
   * Replaces the image collection on a listing with new images.
   * Uses orphanRemoval for clean cascade deletion.
   *
   * @param imageRequests New image request list.
   * @param listing The listing entity to update.
   */
  public void replaceImages(
      List<ImageRequest> imageRequests,
      RentalListingEntity listing) {

    // Clear existing images (orphanRemoval handles deletion)
    listing.getImages().clear();

    if (Objects.nonNull(imageRequests) && !imageRequests.isEmpty()) {
      List<RentalListingImageEntity> newImages =
          buildImageEntities(imageRequests, listing);
      listing.getImages().addAll(newImages);
    }
  }

  /**
   * Replaces the amenity associations on a listing.
   *
   * @param amenities New amenity entities to associate.
   * @param listing The listing entity to update.
   */
  public void replaceAmenities(
      List<AmenityEntity> amenities,
      RentalListingEntity listing) {

    listing.getListingAmenities().clear();

    if (Objects.nonNull(amenities) && !amenities.isEmpty()) {
      List<RentalListingAmenityEntity> newAmenities =
          buildAmenityEntities(amenities, listing);
      listing.getListingAmenities().addAll(newAmenities);
    }
  }

  /**
   * Replaces the amenity location collection on a listing.
   *
   * @param locationRequests New amenity location request list.
   * @param listing The listing entity to update.
   */
  public void replaceAmenityLocations(
      List<AmenityLocationRequest> locationRequests,
      RentalListingEntity listing) {

    listing.getAmenityLocations().clear();

    if (Objects.nonNull(locationRequests)
        && !locationRequests.isEmpty()) {
      List<RentalListingAmenityLocEntity> newLocs =
          buildAmenityLocEntities(locationRequests, listing);
      listing.getAmenityLocations().addAll(newLocs);
    }
  }

  /**
   * Replaces the cost-of-living entity on a listing.
   *
   * @param colRequest New cost-of-living request DTO.
   * @param listing The listing entity to update.
   */
  public void replaceCostOfLiving(
      CostOfLivingRequest colRequest,
      RentalListingEntity listing) {

    if (Objects.nonNull(colRequest)) {
      if (Objects.nonNull(listing.getCostOfLiving())) {
        // Update existing COL entity in place
        RentalListingCOLEntity col = listing.getCostOfLiving();
        col.setElectricityBill(colRequest.getElectricityBill());
        col.setWaterBill(colRequest.getWaterBill());
        col.setServicesFee(colRequest.getServicesFee());
        col.setDeposit(colRequest.getDeposit());
      } else {
        // Create new COL entity
        listing.setCostOfLiving(
            buildCostOfLivingEntity(colRequest, listing));
      }
    }
  }

  /**
   * Maps a {@link RentalListingEntity} to a
   * {@link RentalListingDetailResponse}.
   *
   * @param listing The listing entity to map.
   * @return The detail response DTO.
   */
  public RentalListingDetailResponse mapToDetailResponse(
      RentalListingEntity listing) {

    if (Objects.isNull(listing)) {
      return null;
    }

    return RentalListingDetailResponse.builder()
        .listingId(listing.getListingId())
        .ownerId(listing.getOwner().getUserId())
        .status(listing.getStatus().name())
        .isBoosted(listing.getIsBoosted())
        .type(mapListingType(listing.getType()))
        .title(listing.getTitle())
        .location(listing.getLocation())
        .address(listing.getAddress())
        .area(listing.getArea())
        .price(listing.getPrice())
        .phoneContact(listing.getPhoneContact())
        .accessPolicy(listing.getAccessPolicy())
        .accessOpenTime(listing.getAccessOpenTime())
        .accessCloseTime(listing.getAccessCloseTime())
        .description(listing.getDescription())
        .approvedAt(listing.getApprovedAt())
        .expireAt(listing.getExpireAt())
        .boostExpireAt(listing.getBoostExpireAt())
        .createdAt(listing.getCreatedAt())
        .updatedAt(listing.getUpdatedAt())
        .images(mapImages(listing.getImages()))
        .amenities(mapAmenities(listing.getListingAmenities()))
        .amenityLocations(
            mapAmenityLocations(listing.getAmenityLocations()))
        .costOfLiving(mapCostOfLiving(listing.getCostOfLiving()))
        .build();
  }

  // ---------------------------------------------------------------
  // Private mapping methods
  // ---------------------------------------------------------------

  private ListingTypeResponse mapListingType(
      RentalListingTypeEntity type) {

    if (Objects.isNull(type)) {
      return null;
    }
    return ListingTypeResponse.builder()
        .typeId(type.getTypeId())
        .code(type.getCode())
        .name(type.getName())
        .build();
  }

  private List<ImageResponse> mapImages(
      List<RentalListingImageEntity> images) {

    if (Objects.isNull(images) || images.isEmpty()) {
      return new ArrayList<>();
    }
    return images.stream()
        .sorted(Comparator.comparingInt(
            RentalListingImageEntity::getDisplayOrder))
        .map(img -> ImageResponse.builder()
            .imageId(img.getImageId())
            .imageUrl(img.getImageUrl())
            .displayOrder(img.getDisplayOrder())
            .build())
        .collect(Collectors.toList());
  }

  private List<AmenityResponse> mapAmenities(
      List<RentalListingAmenityEntity> listingAmenities) {

    if (Objects.isNull(listingAmenities)
        || listingAmenities.isEmpty()) {
      return new ArrayList<>();
    }
    return listingAmenities.stream()
        .map(la -> {
          AmenityEntity amenity = la.getAmenity();
          return AmenityResponse.builder()
              .amenityId(amenity.getAmenityId())
              .code(amenity.getCode())
              .name(amenity.getName())
              .icon(amenity.getIcon())
              .build();
        })
        .collect(Collectors.toList());
  }

  private List<AmenityLocationResponse> mapAmenityLocations(
      List<RentalListingAmenityLocEntity> amenityLocations) {

    if (Objects.isNull(amenityLocations)
        || amenityLocations.isEmpty()) {
      return new ArrayList<>();
    }
    return amenityLocations.stream()
        .map(al -> AmenityLocationResponse.builder()
            .amenityLocationId(al.getAmenityLocationId())
            .type(al.getType())
            .description(al.getDescription())
            .distance(al.getDistance())
            .build())
        .collect(Collectors.toList());
  }

  private CostOfLivingResponse mapCostOfLiving(
      RentalListingCOLEntity col) {

    if (Objects.isNull(col)) {
      return null;
    }
    return CostOfLivingResponse.builder()
        .electricityBill(col.getElectricityBill())
        .waterBill(col.getWaterBill())
        .servicesFee(col.getServicesFee())
        .deposit(col.getDeposit())
        .build();
  }
}

