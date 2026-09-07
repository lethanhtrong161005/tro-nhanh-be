package com.tronhanh.dto.request.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request DTO for living costs of a listing.
 */
@Data
@Schema(description = "Request object for living costs of a listing")
public class CostOfLivingRequest {

  @Schema(description = "Electricity price/unit", example = "3500")
  private BigDecimal electricityBill;

  @Schema(description = "Water price/unit", example = "20000")
  private BigDecimal waterBill;

  @Schema(description = "Service/management fee", example = "100000")
  private BigDecimal servicesFee;

  @Schema(description = "Required deposit amount", example = "5000000")
  private BigDecimal deposit;

}
