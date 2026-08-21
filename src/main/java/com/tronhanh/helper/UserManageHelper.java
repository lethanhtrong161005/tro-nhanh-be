package com.tronhanh.helper;

import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.user.manage.UserManageResponse;
import com.tronhanh.entity.UserEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/** Helper class for UserManageService to handle complex mapping and data transformation logic. */
@Component
public class UserManageHelper {

  /**
   * Maps a Spring Data Page of UserEntity to a structured PageResponse of UserManageResponse.
   *
   * @param userPage The raw page of user entities from the database.
   * @return A mapped PageResponse containing user details.
   */
  public PageResponse<UserManageResponse> buildUserManagePageResponse(Page<UserEntity> userPage) {
    if (userPage == null) {
      return null;
    }

    List<UserManageResponse> userResponses =
        userPage.getContent().stream()
            .map(
                user ->
                    UserManageResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .phoneNumber(user.getPhoneNumber())
                        .status(user.getStatus() != null ? user.getStatus().name() : null)
                        .roleName(
                            user.getSystemRoleAssignment() != null
                                    && user.getSystemRoleAssignment().getRole() != null
                                ? user.getSystemRoleAssignment().getRole().getRoleName().name()
                                : null)
                        .build())
            .collect(Collectors.toList());

    return PageResponse.<UserManageResponse>builder()
        .content(userResponses)
        .pageNumber(userPage.getNumber()) // Keep 0-indexed as per PageResponse schema
        .pageSize(userPage.getSize())
        .totalElements(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .last(userPage.isLast())
        .build();
  }
}
