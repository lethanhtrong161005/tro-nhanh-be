package com.tronhanh.dto.request.listing;

import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request DTO for an amenity location near a listing.
 */
@Data
@Schema(description = "Request object for an amenity location near a listing")
public class AmenityLocationRequest {

  @RequireField(field = "Type")
  @Schema(description = "Location type e.g. BUS_STOP, UNIVERSITY", example = "BUS_STOP")
  private String type;

  @Schema(description = "Name/details of surrounding location", example = "Ben Thanh Bus Station")
  private String description;

  @Schema(description = "Distance in km", example = "1.5")
  private BigDecimal distance;

}
