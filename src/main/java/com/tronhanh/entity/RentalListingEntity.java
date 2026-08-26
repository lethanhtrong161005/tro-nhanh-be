package com.tronhanh.entity;

import com.tronhanh.enums.RentalListingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.tronhanh.annotation.UuidV7;

/**
 * Entity representing a rental listing.
 *
 * <p>A rental listing belongs to one user and one listing type, and owns its images, amenity
 * locations, and cost-of-living information.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_listings")
public class RentalListingEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "listing_id", updatable = false, nullable = false)
  private UUID listingId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  private UserEntity owner;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "type_id", nullable = false)
  private RentalListingTypeEntity type;

  @Column(name = "title", nullable = false, length = 255)
  private String title;

  @Column(name = "location", nullable = false, length = 255)
  private String location;

  @Column(name = "address", nullable = false, length = 500)
  private String address;

  @Column(name = "area", nullable = false, precision = 10, scale = 2)
  private BigDecimal area;

  @Column(name = "price", nullable = false, precision = 15, scale = 2)
  private BigDecimal price;

  @Column(name = "phone_contact", length = 20)
  private String phoneContact;

  @Column(name = "access_policy", nullable = false, length = 20)
  private String accessPolicy;

  @Column(name = "access_open_time")
  private LocalTime accessOpenTime;

  @Column(name = "access_close_time")
  private LocalTime accessCloseTime;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  @Builder.Default
  private RentalListingStatus status = RentalListingStatus.DRAFT;

  @Column(name = "is_boosted", nullable = false)
  @Builder.Default
  private Boolean isBoosted = false;

  @Column(name = "approved_at")
  private Instant approvedAt;

  @Column(name = "expire_at")
  private Instant expireAt;

  @Column(name = "boost_expire_at")
  private Instant boostExpireAt;

  @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<RentalListingImageEntity> images = new ArrayList<>();

  @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<RentalListingAmenityEntity> listingAmenities = new ArrayList<>();

  @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<RentalListingAmenityLocEntity> amenityLocations = new ArrayList<>();

  @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
  private RentalListingCOLEntity costOfLiving;
}
