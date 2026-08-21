package com.tronhanh.controller;

import com.tronhanh.dto.request.common.manage.PayloadRequest;
import com.tronhanh.dto.request.user.manage.UserManageRequest;
import com.tronhanh.dto.request.user.manage.UserSortRequest;
import com.tronhanh.dto.response.common.ApiResponse;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.user.manage.UserManageResponse;
import com.tronhanh.service.UserManageService;
import com.tronhanh.util.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for user management operations. Provides endpoints to search, filter,
 * and retrieve user information with pagination and sorting.
 */
@Tag(name = "User Management", description = "Endpoints for managing users in the back-office")
@RestController
@RequestMapping("/api/v1/manage/users")
@RequiredArgsConstructor
public class UserManageController {

  private final UserManageService userManageService;

  /**
   * Retrieves a paginated list of users based on complex search criteria, including dynamic
   * sorting.
   *
   * @param request The payload containing search filters, pagination data, and sorting
   *     configuration.
   * @return A standard API response wrapping the paginated user data.
   */
  @Operation(summary = "Search users with pagination and sorting")
  @PostMapping
  public ResponseEntity<ApiResponse<PageResponse<UserManageResponse>>> searchUsers(
      @Valid @RequestBody PayloadRequest<UserManageRequest, UserSortRequest> request) {
    PageResponse<UserManageResponse> response = userManageService.searchUsers(request);
    return ResponseUtils.successWithData(response);
  }
}
