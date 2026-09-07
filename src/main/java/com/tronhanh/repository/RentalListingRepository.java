package com.tronhanh.repository;

import com.tronhanh.entity.RentalListingEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/** Repository for {@link RentalListingEntity} persistence operations. */
public interface RentalListingRepository
    extends JpaRepository<RentalListingEntity, UUID>,
        JpaSpecificationExecutor<RentalListingEntity> {

  /**
   * Finds an active (non-deleted) rental listing by its ID.
   *
   * @param listingId The listing UUID to search for.
   * @return Optional containing the listing if found and not deleted.
   */
  @Query("SELECT l FROM RentalListingEntity l WHERE l.listingId = ?1 AND l.isDeleted = false")
  Optional<RentalListingEntity> findByListingIdAndIsDeletedFalse(UUID listingId);
}
