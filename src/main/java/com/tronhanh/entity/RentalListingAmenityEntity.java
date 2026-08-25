package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "rental_listing_amenities")
public class RentalListingAmenityEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "amenity_relation_id", updatable = false, nullable = false)
  private UUID amenityRelationId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private RentalListingEntity listing;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "amenity_id", nullable = false)
  private AmenityEntity amenity;
}
