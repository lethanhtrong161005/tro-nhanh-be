package com.tronhanh.entity;

import com.tronhanh.annotation.UuidV7;
import com.tronhanh.enums.BoostOrderStatus;
import com.tronhanh.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a boost purchase order for a rental listing. Tracks the full payment
 * lifecycle from creation to completion. When payment is confirmed (COMPLETED), the associated
 * listing's isBoosted is set to true and boostExpireAt is calculated. Editing a listing is not
 * allowed while it is boosted. Only ACTIVE listings can create boost orders.
 *
 * @see BoostPackageEntity
 * @see RentalListingEntity
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "boost_orders")
public class BoostOrderEntity extends BaseEntity
{

  @Id
  @UuidV7
  @Column(name = "order_id", updatable = false, nullable = false)
  private UUID orderId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "listing_id", nullable = false)
  private RentalListingEntity listing;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "package_id", nullable = false)
  private BoostPackageEntity boostPackage;

  @Column(name = "amount", nullable = false, precision = 15, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", nullable = false, length = 30)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  @Builder.Default
  private BoostOrderStatus status = BoostOrderStatus.PENDING;

  @Column(name = "transaction_id", length = 255)
  private String transactionId;

  @Column(name = "paid_at")
  private Instant paidAt;

  @Column(name = "expire_at")
  private Instant expireAt;
}
