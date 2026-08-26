package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Master data entity representing a boost package that users can purchase. Each package defines a
 * duration (in days) and price for boosting a rental listing. Packages are managed by Admin and can
 * be activated or deactivated via {@link #isActive}.
 *
 * @see BoostOrderEntity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boost_packages")
public class BoostPackageEntity extends BaseEntity
{

  @Id
  @UuidV7
  @Column(name = "package_id", updatable = false, nullable = false)
  private UUID packageId;

  @Column(name = "code", nullable = false, unique = true, length = 20)
  private String code;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "duration_days", nullable = false)
  private Integer durationDays;

  @Column(name = "price", nullable = false, precision = 15, scale = 2)
  private BigDecimal price;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private Boolean isActive = true;

  @OneToMany(mappedBy = "boostPackage")
  @Builder.Default
  private List<BoostOrderEntity> boostOrders = new ArrayList<>();
}
