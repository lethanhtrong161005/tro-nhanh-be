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
@Table(name = "rental_listing_images")
public class RentalListingImageEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "image_id", updatable = false, nullable = false)
  private UUID imageId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private RentalListingEntity listing;

  @Column(name = "image_url", nullable = false, length = 1000)
  private String imageUrl;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder;
}