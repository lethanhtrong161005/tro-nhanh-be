package com.tronhanh.helper;

import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.entity.UserProfileEntity;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Helper component for mapping, initializing, and updating {@link UserProfileEntity} instances.
 * Extracts business transformation logic away from the service layer.
 *
 * @see UserProfileEntity
 * @see UserProfileResponse
 */
@Component
public class UserProfileHelper
{

  /**
   * Maps {@link UserProfileEntity} and its associated {@link UserEntity} to {@link UserProfileResponse}.
   *
   * @param profile The UserProfileEntity to map.
   * @return Populated UserProfileResponse, or null if input profile is null.
   */
  public UserProfileResponse mapToUserProfileResponse(UserProfileEntity profile) {
    if (Objects.isNull(profile)) {
      return null;
    }

    UserEntity user = profile.getUser();

    // Map user personal data combined with extended profile demographics
    return UserProfileResponse.builder()
        .userProfileId(profile.getUserProfileId())
        .userId(Objects.nonNull(user) ? user.getUserId() : null)
        .email(Objects.nonNull(user) ? user.getEmail() : null)
        .firstName(Objects.nonNull(user) ? user.getFirstName() : null)
        .lastName(Objects.nonNull(user) ? user.getLastName() : null)
        .fullName(Objects.nonNull(user) ? user.getFullName() : null)
        .phoneNumber(Objects.nonNull(user) ? user.getPhoneNumber() : null)
        .avatarUrl(profile.getAvatarUrl())
        .dateOfBirth(profile.getDateOfBirth())
        .gender(profile.getGender())
        .bio(profile.getBio())
        .occupation(profile.getOccupation())
        .address(profile.getAddress())
        .city(profile.getCity())
        .district(profile.getDistrict())
        .ward(profile.getWard())
        .createdAt(profile.getCreatedAt())
        .updatedAt(profile.getUpdatedAt())
        .build();
  }

  /**
   * Instantiates a new {@link UserProfileEntity} populated with request data for a given user.
   *
   * @param user The owner UserEntity.
   * @param request The update payload containing initial profile field values.
   * @return Newly configured UserProfileEntity instance.
   */
  public UserProfileEntity buildNewUserProfile(UserEntity user, UpdateUserProfileRequest request) {
    return UserProfileEntity.builder()
        .user(user)
        .avatarUrl(request.getAvatarUrl())
        .dateOfBirth(request.getDateOfBirth())
        .gender(request.getGender())
        .bio(request.getBio())
        .occupation(request.getOccupation())
        .address(request.getAddress())
        .city(request.getCity())
        .district(request.getDistrict())
        .ward(request.getWard())
        .build();
  }

  /**
   * Performs full replacement of all mutable profile fields from an {@link UpdateUserProfileRequest}.
   *
   * @param profile The existing UserProfileEntity to mutate.
   * @param request The payload with new field values.
   */
  public void updateProfile(UserProfileEntity profile, UpdateUserProfileRequest request) {
    // Overwrite all profile attributes with incoming request values
    profile.setAvatarUrl(request.getAvatarUrl());
    profile.setDateOfBirth(request.getDateOfBirth());
    profile.setGender(request.getGender());
    profile.setBio(request.getBio());
    profile.setOccupation(request.getOccupation());
    profile.setAddress(request.getAddress());
    profile.setCity(request.getCity());
    profile.setDistrict(request.getDistrict());
    profile.setWard(request.getWard());
    profile.setIsDeleted(false);
  }

  /**
   * Performs partial mutation of profile fields from a {@link PatchUserProfileRequest}.
   * Only non-null properties in the request will overwrite existing entity values.
   *
   * @param profile The existing UserProfileEntity to mutate.
   * @param request The payload containing partial fields.
   */
  public void patchProfile(UserProfileEntity profile, PatchUserProfileRequest request) {
    // Check and apply only non-null patch fields
    if (Objects.nonNull(request.getAvatarUrl())) {
      profile.setAvatarUrl(request.getAvatarUrl());
    }
    if (Objects.nonNull(request.getDateOfBirth())) {
      profile.setDateOfBirth(request.getDateOfBirth());
    }
    if (Objects.nonNull(request.getGender())) {
      profile.setGender(request.getGender());
    }
    if (Objects.nonNull(request.getBio())) {
      profile.setBio(request.getBio());
    }
    if (Objects.nonNull(request.getOccupation())) {
      profile.setOccupation(request.getOccupation());
    }
    if (Objects.nonNull(request.getAddress())) {
      profile.setAddress(request.getAddress());
    }
    if (Objects.nonNull(request.getCity())) {
      profile.setCity(request.getCity());
    }
    if (Objects.nonNull(request.getDistrict())) {
      profile.setDistrict(request.getDistrict());
    }
    if (Objects.nonNull(request.getWard())) {
      profile.setWard(request.getWard());
    }
  }
}