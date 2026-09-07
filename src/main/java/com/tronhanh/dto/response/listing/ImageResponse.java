package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

/**
 * Image Response DTO.
 */
@Data
@Builder
@Schema(description = "Image Response DTO")
public class ImageResponse
{
  /**
   * Image ID
   */
  private UUID imageId;

  /**
   * Image URL
   */
  private String imageUrl;

  /**
   * Display order
   */
  private Integer displayOrder;
}
