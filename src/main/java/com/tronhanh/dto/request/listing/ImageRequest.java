package com.tronhanh.dto.request.listing;

import com.tronhanh.validation.RequireField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Request DTO for an image in a listing.
 */
@Data
@Schema(description = "Request object for an image in a listing")
public class ImageRequest {

  @RequireField(field = "Image URL")
  @Schema(description = "Image URL", example = "https://example.com/image.jpg")
  private String imageUrl;

  @RequireField(field = "Display Order")
  @Schema(description = "Display sequence order", example = "1")
  private Integer displayOrder;

}
