package com.tronhanh.dto.request.listing;

import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a new rental listing.
 */
@Data
@Schema(description = "Request object for creating a new rental listing")
public class CreateRentalListingRequest {

  @RequireField(field = "Type ID")
  @Schema(description = "Reference to listing type", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID typeId;

  @RequireField(field = "Title")
  @Schema(description = "Listing title", example = "Beautiful studio in District 1")
  private String title;

  @RequireField(field = "Location")
  @Schema(description = "General location/area", example = "District 1, HCMC")
  private String location;

  @RequireField(field = "Address")
  @Schema(description = "Detailed address", example = "123 Le Loi, Ben Nghe Ward")
  private String address;

  @RequireField(field = "Area")
  @Schema(description = "Area in m2", example = "35.5")
  private BigDecimal area;

  @RequireField(field = "Price")
  @Schema(description = "Monthly rental price VND", example = "5000000")
  private BigDecimal price;

  @Schema(description = "Contact phone number", example = "0987654321")
  private String phoneContact;

  @RequireField(field = "Access Policy")
  @Schema(description = "Access policy e.g. FREE, CURFEW", example = "FREE")
  private String accessPolicy;

  @Schema(description = "Gate opening time", example = "05:00:00")
  private LocalTime accessOpenTime;

  @Schema(description = "Gate closing time", example = "23:00:00")
  private LocalTime accessCloseTime;

  @Schema(description = "Detailed description", example = "A very nice studio apartment...")
  private String description;

  @Valid
  @Schema(description = "Image list")
  private List<ImageRequest> images;

  @Schema(description = "Selected predefined amenity IDs", example = "[\"123e4567-e89b-12d3-a456-426614174000\"]")
  private List<UUID> amenityIds;

  @Valid
  @Schema(description = "Surrounding locations")
  private List<AmenityLocationRequest> amenityLocations;

  @Valid
  @Schema(description = "Living costs")
  private CostOfLivingRequest costOfLiving;

}
