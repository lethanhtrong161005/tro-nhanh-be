package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * Amenity Response DTO.
 */
@Data
@Builder
@Schema(description = "Amenity Response DTO")
public class AmenityResponse
{
  /**
   * Amenity ID
   */
  private UUID amenityId;

  /**
   * Amenity code
   */
  private String code;

  /**
   * Amenity name
   */
  private String name;

  /**
   * Amenity icon
   */
  private String icon;
}
