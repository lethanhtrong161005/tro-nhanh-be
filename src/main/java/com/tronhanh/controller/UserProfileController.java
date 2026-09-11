package com.tronhanh.controller;

import com.tronhanh.constant.MessageCodeConstant;
import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import com.tronhanh.service.UserProfileService;
import com.tronhanh.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller providing user profile management endpoints.
 * Includes self-service profile operations for authenticated users and administrative operations.
 */
@Tag(name = "User Profile", description = "Endpoints for managing user profile information")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserProfileController
{

  /** User profile business service component. */
  private final UserProfileService userProfileService;

  /**
   * Retrieves profile details of the currently authenticated user.
   *
   * @return Standardized ApiResponse containing UserProfileResponse.
   */
  @Operation(summary = "Get current user profile")
  @GetMapping("/me/profile")
  public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile() {
    UserProfileResponse response = userProfileService.getCurrentUserProfile();
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Performs full update or creation of the authenticated user's profile.
   *
   * @param request UpdateUserProfileRequest payload.
   * @return Standardized ApiResponse containing updated UserProfileResponse.
   */
  @Operation(summary = "Full update or create current user profile")
  @PutMapping("/me/profile")
  public ResponseEntity<ApiResponse<UserProfileResponse>> updateCurrentUserProfile(
      @Valid @RequestBody UpdateUserProfileRequest request) {
    UserProfileResponse response = userProfileService.updateCurrentUserProfile(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_003, "UserProfile");
  }

  /**
   * Performs partial update on specific fields of the authenticated user's profile.
   *
   * @param request PatchUserProfileRequest payload.
   * @return Standardized ApiResponse containing updated UserProfileResponse.
   */
  @Operation(summary = "Partial update current user profile")
  @PatchMapping("/me/profile")
  public ResponseEntity<ApiResponse<UserProfileResponse>> patchCurrentUserProfile(
      @Valid @RequestBody PatchUserProfileRequest request) {
    UserProfileResponse response = userProfileService.patchCurrentUserProfile(request);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_003, "UserProfile");
  }

  /**
   * Retrieves profile details of any user by user ID (Admin only).
   *
   * @param userId Unique identifier of the target user.
   * @return Standardized ApiResponse containing target user's UserProfileResponse.
   */
  @Operation(summary = "Get user profile by user ID (Admin only)")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/{userId}/profile")
  public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfileByAdmin(
      @PathVariable("userId") UUID userId) {
    UserProfileResponse response = userProfileService.getUserProfileByAdmin(userId);
    return ResponseUtils.successWithData(response, MessageCodeConstant.MSG_CODE_001);
  }

  /**
   * Soft-deletes a user profile by user ID (Admin only).
   *
   * @param userId Unique identifier of the target user.
   * @return Standardized ApiResponse indicating successful deletion.
   */
  @Operation(summary = "Soft delete user profile by user ID (Admin only)")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{userId}/profile")
  public ResponseEntity<ApiResponse<Void>> deleteUserProfileByAdmin(
      @PathVariable("userId") UUID userId) {
    userProfileService.deleteUserProfileByAdmin(userId);
    return ResponseUtils.success(MessageCodeConstant.MSG_CODE_004, "UserProfile");
  }
}