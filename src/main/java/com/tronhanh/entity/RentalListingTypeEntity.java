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
@Table(name = "rental_listing_types")
public class RentalListingTypeEntity extends BaseEntity {

  @Id
  @UuidV7
  @Column(name = "type_id", updatable = false, nullable = false)
  private UUID typeId;

  @Column(name = "code", nullable = false, unique = true, length = 20)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "type")
  @Builder.Default
  private List<RentalListingEntity> listings = new ArrayList<>();
}
