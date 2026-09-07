package com.tronhanh.dto.request.listing;

import com.tronhanh.enums.RentalListingStatus;
import com.tronhanh.enums.SortOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request parameters for searching, filtering, sorting, and paginating rental listings. */
@Schema(description = "Search and filter parameters for rental listings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalListingSearchRequest {

  @Schema(description = "Keyword matched against title or description", example = "home")
  private String keyword;

  @Schema(description = "Case-insensitive partial location filter", example = "Hồ Chí Minh")
  private String location;

  @Schema(description = "Rental listing type identifier")
  private UUID typeId;

  @Schema(description = "Minimum rental price", example = "2000000")
  @DecimalMin(value = "0", message = "Minimum price must not be negative")
  private BigDecimal minPrice;

  @Schema(description = "Maximum rental price", example = "8000000")
  @DecimalMin(value = "0", message = "Maximum price must not be negative")
  private BigDecimal maxPrice;

  @Schema(description = "Listing status", example = "ACTIVE")
  private RentalListingStatus status;

  @Schema(description = "One-indexed page number", example = "1", defaultValue = "1")
  @Min(value = 1, message = "Page number must be greater than or equal to 1")
  @Builder.Default
  private Integer pageNumber = 1;

  @Schema(description = "Number of records per page", example = "10", defaultValue = "10")
  @Min(value = 1, message = "Page size must be greater than or equal to 1")
  @Max(value = 100, message = "Page size must not exceed 100")
  @Builder.Default
  private Integer pageSize = 10;

  @Schema(
      description = "Sort field: createdAt, updatedAt, price, area, or title",
      example = "createdAt",
      defaultValue = "createdAt")
  @Builder.Default
  private String sortBy = "createdAt";

  @Schema(description = "Sort direction", example = "DESC", defaultValue = "DESC")
  @Builder.Default
  private SortOrder sortOrder = SortOrder.DESC;
}
