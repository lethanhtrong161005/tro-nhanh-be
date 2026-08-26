package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_listing_cost_of_living")
public class RentalListingCOLEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "cost_of_living_id", updatable = false, nullable = false)
  private UUID costOfLivingId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false, unique = true)
  private RentalListingEntity listing;

  @Column(name = "electricity_bill", precision = 15, scale = 2)
  private BigDecimal electricityBill;

  @Column(name = "water_bill", precision = 15, scale = 2)
  private BigDecimal waterBill;

  @Column(name = "services_fee", precision = 15, scale = 2)
  private BigDecimal servicesFee;

  @Column(name = "deposit", precision = 15, scale = 2)
  private BigDecimal deposit;
}
