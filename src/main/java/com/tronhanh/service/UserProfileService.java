package com.tronhanh.service;

import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import java.util.UUID;

/**
 * Service interface defining business operations for managing user profiles.
 * Supports both self-service operations for authenticated users and administration operations.
 */
public interface UserProfileService
{

  /**
   * Retrieves profile information of the currently authenticated user.
   *
   * @return UserProfileResponse containing profile details.
   */
  UserProfileResponse getCurrentUserProfile();

  /**
   * Performs full update or creation (upsert) of the authenticated user's profile.
   *
   * @param request Payload containing full profile field values.
   * @return UserProfileResponse containing updated profile details.
   */
  UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request);

  /**
   * Performs partial update (PATCH) of the authenticated user's profile.
   *
   * @param request Payload containing partial profile fields to update.
   * @return UserProfileResponse containing updated profile details.
   */
  UserProfileResponse patchCurrentUserProfile(PatchUserProfileRequest request);

  /**
   * Retrieves profile information of any user by their user ID (Admin operation).
   *
   * @param userId Unique identifier of the target user.
   * @return UserProfileResponse containing target user's profile details.
   */
  UserProfileResponse getUserProfileByAdmin(UUID userId);

  /**
   * Soft-deletes a user's profile by their user ID (Admin operation).
   *
   * @param userId Unique identifier of the target user.
   */
  void deleteUserProfileByAdmin(UUID userId);
}