package com.tronhanh.repository;

import com.tronhanh.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository interface for {@link UserEntity} persistence operations.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>
{

  /**
   * Finds active (non-deleted) user by phone number.
   *
   * @param phoneNumber The phone number string to search for.
   * @return Optional containing UserEntity if found.
   */
  Optional<UserEntity> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);
}
