package com.tronhanh.service;

import com.tronhanh.dto.request.common.manage.PayloadRequest;
import com.tronhanh.dto.request.user.manage.UserManageRequest;
import com.tronhanh.dto.request.user.manage.UserSortRequest;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.user.manage.UserManageResponse;

/** Service interface for user management operations. */
public interface UserManageService {

  /**
   * Searches and paginates users based on the given payload.
   *
   * @param request Search payload containing pagination, sorting, and filter criteria.
   * @return Paginated response of user information.
   */
  PageResponse<UserManageResponse> searchUsers(
      PayloadRequest<UserManageRequest, UserSortRequest> request);
}
