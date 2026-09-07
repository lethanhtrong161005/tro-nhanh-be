package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * Listing Type Response DTO.
 */
@Data
@Builder
@Schema(description = "Listing Type Response DTO")
public class ListingTypeResponse
{
  /**
   * Unique listing type identifier
   */
  private UUID typeId;

  /**
   * Listing type code
   */
  private String code;

  /**
   * Listing type name
   */
  private String name;
}
