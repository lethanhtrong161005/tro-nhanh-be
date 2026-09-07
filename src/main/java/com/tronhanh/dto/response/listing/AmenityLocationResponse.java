package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * Amenity Location Response DTO.
 */
@Data
@Builder
@Schema(description = "Amenity Location Response DTO")
public class AmenityLocationResponse
{
  /**
   * Amenity location ID
   */
  private UUID amenityLocationId;

  /**
   * Location type
   */
  private String type;

  /**
   * Location description
   */
  private String description;

  /**
   * Distance
   */
  private BigDecimal distance;
}
