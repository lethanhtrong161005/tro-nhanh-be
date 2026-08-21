package com.tronhanh.helper;

import com.tronhanh.dto.response.auth.UserProfileResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.enums.RoleName;
import java.util.Objects;
import org.springframework.stereotype.Component;

/** Helper component for mapping {@link UserEntity} to DTO responses. */
@Component
public class UserHelper {

  /**
   * Maps {@link UserEntity} to {@link UserProfileResponse}.
   *
   * @param user UserEntity instance.
   * @return UserProfileResponse instance or null if input user is null.
   */
  public UserProfileResponse mapToUserProfileResponse(UserEntity user) {
    if (Objects.isNull(user)) {
      return null;
    }

    RoleName roleName =
        (Objects.nonNull(user.getSystemRoleAssignment())
                && Objects.nonNull(user.getSystemRoleAssignment().getRole()))
            ? user.getSystemRoleAssignment().getRole().getRoleName()
            : null;

    return UserProfileResponse.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .fullName(user.getFullName())
        .phoneNumber(user.getPhoneNumber())
        .status(user.getStatus())
        .roleName(roleName)
        .build();
  }
}
