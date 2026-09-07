package com.tronhanh.repository;

import com.tronhanh.entity.RentalListingTypeEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Repository for {@link RentalListingTypeEntity} persistence operations. */
public interface RentalListingTypeRepository
    extends JpaRepository<RentalListingTypeEntity, UUID> {

  /**
   * Finds an active (non-deleted) listing type by its ID.
   *
   * @param typeId The listing type UUID.
   * @return Optional containing the type if found and not deleted.
   */
  @Query("SELECT t FROM RentalListingTypeEntity t WHERE t.typeId = ?1 AND t.isDeleted = false")
  Optional<RentalListingTypeEntity> findByTypeIdAndIsDeletedFalse(UUID typeId);
}
