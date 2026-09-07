package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * Rental Listing Detail Response DTO.
 */
@Data
@Builder
@Schema(description = "Rental Listing Detail Response DTO")
public class RentalListingDetailResponse
{
  /**
   * Unique listing identifier
   */
  private UUID listingId;

  /**
   * Owner user ID
   */
  private UUID ownerId;

  /**
   * Listing status (DRAFT, PENDING_APPROVAL, ACTIVE, etc.)
   */
  private String status;

  /**
   * Whether listing is currently boosted
   */
  private Boolean isBoosted;

  /**
   * Hydrated listing type details
   */
  private ListingTypeResponse type;

  /**
   * Listing title
   */
  private String title;

  /**
   * General location
   */
  private String location;

  /**
   * Detailed address
   */
  private String address;

  /**
   * Area in m2
   */
  private BigDecimal area;

  /**
   * Monthly rental price VND
   */
  private BigDecimal price;

  /**
   * Contact phone
   */
  private String phoneContact;

  /**
   * Access policy
   */
  private String accessPolicy;

  /**
   * Gate opening time
   */
  private LocalTime accessOpenTime;

  /**
   * Gate closing time
   */
  private LocalTime accessCloseTime;

  /**
   * Description
   */
  private String description;

  /**
   * Admin approval timestamp
   */
  private Instant approvedAt;

  /**
   * Listing expiry timestamp
   */
  private Instant expireAt;

  /**
   * Boost expiration
   */
  private Instant boostExpireAt;

  /**
   * Creation time
   */
  private Instant createdAt;

  /**
   * Last update time
   */
  private Instant updatedAt;

  /**
   * Images sorted by displayOrder
   */
  private List<ImageResponse> images;

  /**
   * Predefined amenities
   */
  private List<AmenityResponse> amenities;

  /**
   * Surrounding locations
   */
  private List<AmenityLocationResponse> amenityLocations;

  /**
   * Living costs
   */
  private CostOfLivingResponse costOfLiving;
}
