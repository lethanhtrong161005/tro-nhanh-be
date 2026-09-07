package com.tronhanh.dto.response.listing;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * Cost Of Living Response DTO.
 */
@Data
@Builder
@Schema(description = "Cost Of Living Response DTO")
public class CostOfLivingResponse
{
  /**
   * Electricity bill
   */
  private BigDecimal electricityBill;

  /**
   * Water bill
   */
  private BigDecimal waterBill;

  /**
   * Services fee
   */
  private BigDecimal servicesFee;

  /**
   * Deposit
   */
  private BigDecimal deposit;
}
