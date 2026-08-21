package com.tronhanh.repository;

import com.tronhanh.dto.request.user.manage.UserManageRequest;
import com.tronhanh.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/** Spring Data JPA Repository interface for {@link UserEntity} persistence operations. */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  /**
   * Finds active (non-deleted) user by phone number and eagerly fetches role assignments.
   *
   * @param phoneNumber The phone number string to search for.
   * @return Optional containing UserEntity if found.
   */
  @EntityGraph(attributePaths = {"systemRoleAssignment", "systemRoleAssignment.role"})
  Optional<UserEntity> findByPhoneNumberAndIsDeletedFalse(String phoneNumber);

  /**
   * Finds active (non-deleted) user by ID and eagerly fetches role assignments.
   *
   * @param userId The UUID of the user.
   * @return Optional containing UserEntity if found.
   */
  @EntityGraph(attributePaths = {"systemRoleAssignment", "systemRoleAssignment.role"})
  Optional<UserEntity> findByUserIdAndIsDeletedFalse(UUID userId);

  /**
   * Generic dynamic search for users using SpEL.
   *
   * @param req Search criteria payload.
   * @param pageable Pagination and sorting configuration.
   * @return Page of UserEntity matching criteria.
   */
  @Query(
      "SELECT u FROM UserEntity u "
          + "LEFT JOIN u.systemRoleAssignment sra "
          + "WHERE (:#{#req?.email} IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :#{#req?.email}, '%')) ESCAPE '\\') "
          + "AND (:#{#req?.fullName} IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :#{#req?.fullName}, '%')) ESCAPE '\\') "
          + "AND (:#{#req?.phoneNumber} IS NULL OR u.phoneNumber LIKE CONCAT('%', :#{#req?.phoneNumber}, '%') ESCAPE '\\') "
          + "AND (:#{#req?.roleId} IS NULL OR CAST(sra.role.roleId AS string) = :#{#req?.roleId}) "
          + "AND u.isDeleted = false")
  Page<UserEntity> searchUsers(@Param("req") UserManageRequest req, Pageable pageable);

}
