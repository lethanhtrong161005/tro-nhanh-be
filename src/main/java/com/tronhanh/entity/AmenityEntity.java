package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "amenities")
public class AmenityEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "amenity_id", updatable = false, nullable = false)
  private UUID amenityId;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "icon", length = 255)
  private String icon;

  @OneToMany(mappedBy = "amenity")
  @Builder.Default
  private List<RentalListingAmenityEntity> listingAmenities = new ArrayList<>();
}