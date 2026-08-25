package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "rental_listing_amenity_locations")
public class RentalListingAmenityLocEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "amenity_location_id", updatable = false, nullable = false)
  private UUID amenityLocationId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private RentalListingEntity listing;

  @Column(name = "type", nullable = false, length = 100)
  private String type;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "distance", precision = 10, scale = 2)
  private BigDecimal distance;
}
