package com.tronhanh.repository;

import com.tronhanh.entity.AmenityEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Repository for {@link AmenityEntity} persistence operations. */
public interface AmenityRepository extends JpaRepository<AmenityEntity, UUID> {

  /**
   * Finds all active amenities matching the provided IDs.
   *
   * @param amenityIds List of amenity UUIDs to search for.
   * @return List of matching active amenity entities.
   */
  @Query("SELECT a FROM AmenityEntity a WHERE a.amenityId IN ?1 AND a.isDeleted = false")
  List<AmenityEntity> findAllByAmenityIdInAndIsDeletedFalse(List<UUID> amenityIds);
}
