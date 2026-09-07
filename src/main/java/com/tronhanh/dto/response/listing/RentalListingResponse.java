package com.tronhanh.dto.response.listing;

import com.tronhanh.enums.RentalListingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Summary representation of a rental listing returned by paginated searches. */
@Schema(description = "Rental listing summary")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalListingResponse {

  @Schema(
      description = "Rental listing identifier",
      example = "0198a4a8-3fd2-7000-8000-000000000001")
  private UUID listingId;

  @Schema(description = "Listing title", example = "Modern studio near city center")
  private String title;

  @Schema(description = "General location", example = "Hồ Chí Minh")
  private String location;

  @Schema(description = "Listing address", example = "District 1, Hồ Chí Minh City")
  private String address;

  @Schema(description = "Area in square meters", example = "28.50")
  private BigDecimal area;

  @Schema(description = "Rental price", example = "5500000")
  private BigDecimal price;

  @Schema(description = "Listing status", example = "ACTIVE")
  private RentalListingStatus status;

  @Schema(description = "Whether the listing is boosted", example = "true")
  private Boolean isBoosted;

  @Schema(description = "Rental listing type identifier")
  private UUID typeId;

  @Schema(description = "Rental listing type name", example = "Studio")
  private String typeName;

  @Schema(description = "Listing creation timestamp")
  private Instant createdAt;
}
