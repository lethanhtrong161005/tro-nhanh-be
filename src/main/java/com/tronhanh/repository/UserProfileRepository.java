package com.tronhanh.repository;

import com.tronhanh.entity.UserProfileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Data access repository for {@link UserProfileEntity}.
 * All retrieval methods strictly filter out soft-deleted records.
 *
 * @see UserProfileEntity
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID>
{

  /**
   * Finds an active (non-deleted) user profile by the associated user's ID.
   *
   * @param userId Unique identifier of the user.
   * @return Optional containing UserProfileEntity if found and not deleted.
   */
  @Query("SELECT up FROM UserProfileEntity up WHERE up.user.userId = :userId AND up.isDeleted = false")
  Optional<UserProfileEntity> findByUserIdActive(@Param("userId") UUID userId);

  /**
   * Finds an active (non-deleted) user profile by its profile ID.
   *
   * @param profileId Unique identifier of the user profile.
   * @return Optional containing UserProfileEntity if found and not deleted.
   */
  @Query("SELECT up FROM UserProfileEntity up WHERE up.userProfileId = :profileId AND up.isDeleted = false")
  Optional<UserProfileEntity> findByIdActive(@Param("profileId") UUID profileId);

  /**
   * Checks whether an active user profile exists for the given user ID.
   *
   * @param userId Unique identifier of the user.
   * @return True if an active profile exists, false otherwise.
   */
  @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END FROM UserProfileEntity up WHERE up.user.userId = :userId AND up.isDeleted = false")
  boolean existsByUserIdActive(@Param("userId") UUID userId);
}