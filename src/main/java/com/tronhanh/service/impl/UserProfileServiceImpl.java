package com.tronhanh.service.impl;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.entity.UserProfileEntity;
import com.tronhanh.exception.HttpException;
import com.tronhanh.helper.UserProfileHelper;
import com.tronhanh.repository.UserProfileRepository;
import com.tronhanh.repository.UserRepository;
import com.tronhanh.service.UserProfileService;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing user profile operations.
 * Handles profile retrieval, upsert, partial patch updates, and administrative soft deletion.
 *
 * @see UserProfileService
 * @see UserProfileHelper
 * @see UserProfileRepository
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService
{

  /** User profile data access repository. */
  private final UserProfileRepository userProfileRepository;

  /** User data access repository. */
  private final UserRepository userRepository;

  /** Profile mapping and transformation helper component. */
  private final UserProfileHelper userProfileHelper;

  /**
   * Retrieves profile information of the currently authenticated user.
   *
   * Step-by-step flow:
   * 1. Extract authenticated UserEntity from SecurityContext.
   * 2. Query database for an active (non-deleted) profile.
   * 3. Throw 404 NOT_FOUND if profile does not exist.
   * 4. Map and return UserProfileResponse.
   *
   * @return Populated UserProfileResponse.
   * @throws HttpException if user is unauthenticated (401) or profile not found (404).
   */
  @Override
  @Transactional(readOnly = true)
  public UserProfileResponse getCurrentUserProfile() {
    UserEntity currentUser = getAuthenticatedUser();

    // Query active profile linked to current user
    UserProfileEntity profile = userProfileRepository
        .findByUserIdActive(currentUser.getUserId())
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "UserProfile"
        ));

    return userProfileHelper.mapToUserProfileResponse(profile);
  }

  /**
   * Performs full update or creation (upsert) of the authenticated user's profile.
   *
   * Step-by-step flow:
   * 1. Extract authenticated UserEntity from SecurityContext.
   * 2. Check if a profile record already exists (including soft-deleted).
   * 3. If exists, overwrite all fields and ensure is_deleted is false.
   * 4. If not exists, build a new UserProfileEntity linked to user.
   * 5. Save entity to database and return response.
   *
   * @param request The full profile update request payload.
   * @return Updated UserProfileResponse.
   * @throws HttpException if user is unauthenticated (401).
   */
  @Override
  @Transactional
  public UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request) {
    UserEntity currentUser = getAuthenticatedUser();

    // Look up active profile for authenticated user
    Optional<UserProfileEntity> existingProfileOpt =
        userProfileRepository.findByUserIdActive(currentUser.getUserId());

    UserProfileEntity profileToSave;
    if (existingProfileOpt.isPresent()) {
      // Full update of existing active profile
      profileToSave = existingProfileOpt.get();
      userProfileHelper.updateProfile(profileToSave, request);
      log.debug("Updating existing profile for user: {}", currentUser.getUserId());
    } else {
      // Create new profile record linked to the authenticated user
      profileToSave = userProfileHelper.buildNewUserProfile(currentUser, request);
      log.info("Creating new user profile for user: {}", currentUser.getUserId());
    }

    UserProfileEntity savedProfile = userProfileRepository.save(profileToSave);
    return userProfileHelper.mapToUserProfileResponse(savedProfile);
  }

  /**
   * Performs partial update (PATCH) of the authenticated user's profile.
   *
   * Step-by-step flow:
   * 1. Extract authenticated UserEntity from SecurityContext.
   * 2. Look up existing active profile; throw 404 NOT_FOUND if absent.
   * 3. Apply only non-null fields from request payload.
   * 4. Save updated profile entity and return response.
   *
   * @param request The partial profile update request payload.
   * @return Updated UserProfileResponse.
   * @throws HttpException if user is unauthenticated (401) or profile not found (404).
   */
  @Override
  @Transactional
  public UserProfileResponse patchCurrentUserProfile(PatchUserProfileRequest request) {
    UserEntity currentUser = getAuthenticatedUser();

    // Fetch existing active profile
    UserProfileEntity profile = userProfileRepository
        .findByUserIdActive(currentUser.getUserId())
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "UserProfile"
        ));

    // Mutate only non-null patch fields
    userProfileHelper.patchProfile(profile, request);

    UserProfileEntity updatedProfile = userProfileRepository.save(profile);
    log.debug("Patched profile for user: {}", currentUser.getUserId());
    return userProfileHelper.mapToUserProfileResponse(updatedProfile);
  }

  /**
   * Retrieves profile information of any user by their user ID (Admin operation).
   *
   * Step-by-step flow:
   * 1. Validate that the target user exists in database and is active.
   * 2. Query target user's active profile; throw 404 NOT_FOUND if absent.
   * 3. Map and return UserProfileResponse.
   *
   * @param userId Unique identifier of target user.
   * @return UserProfileResponse containing target user's profile details.
   * @throws HttpException if target user (404) or profile (404) not found.
   */
  @Override
  @Transactional(readOnly = true)
  public UserProfileResponse getUserProfileByAdmin(UUID userId) {
    // Validate target user existence
    userRepository.findByUserIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "User"
        ));

    // Look up target user's active profile
    UserProfileEntity profile = userProfileRepository
        .findByUserIdActive(userId)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "UserProfile"
        ));

    return userProfileHelper.mapToUserProfileResponse(profile);
  }

  /**
   * Soft-deletes a user's profile by their user ID (Admin operation).
   *
   * Step-by-step flow:
   * 1. Validate that the target user exists.
   * 2. Look up target user's active profile; throw 404 NOT_FOUND if absent.
   * 3. Set isDeleted = true on the profile entity.
   * 4. Save entity to persist soft-deletion and auditing metadata.
   *
   * @param userId Unique identifier of target user.
   * @throws HttpException if target user (404) or profile (404) not found.
   */
  @Override
  @Transactional
  public void deleteUserProfileByAdmin(UUID userId) {
    // Validate target user existence
    userRepository.findByUserIdAndIsDeletedFalse(userId)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "User"
        ));

    // Find target user's active profile
    UserProfileEntity profile = userProfileRepository
        .findByUserIdActive(userId)
        .orElseThrow(() -> new HttpException(
            HttpStatus.NOT_FOUND,
            MessageCodeConstant.MSG_CODE_103,
            "UserProfile"
        ));

    // Perform soft deletion
    profile.setIsDeleted(true);
    userProfileRepository.save(profile);
    log.info("Soft-deleted user profile for user: {}", userId);
  }

  /**
   * Helper method to extract the currently authenticated UserEntity from Spring SecurityContext.
   *
   * @return The authenticated UserEntity principal.
   * @throws HttpException with 401 UNAUTHORIZED if not authenticated.
   */
  private UserEntity getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.isNull(authentication)
        || !(authentication.getPrincipal() instanceof UserEntity user)) {
      throw new HttpException(HttpStatus.UNAUTHORIZED, MessageCodeConstant.MSG_CODE_101);
    }
    return user;
  }
}