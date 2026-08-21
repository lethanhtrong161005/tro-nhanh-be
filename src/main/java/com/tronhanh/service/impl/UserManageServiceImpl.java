package com.tronhanh.service.impl;

import com.tronhanh.dto.request.common.manage.PayloadRequest;
import com.tronhanh.dto.request.common.manage.PageItem;
import com.tronhanh.dto.request.user.manage.UserManageSortCondition;
import com.tronhanh.dto.request.user.manage.UserManageRequest;
import com.tronhanh.dto.request.user.manage.UserSortRequest;
import com.tronhanh.dto.response.common.PageResponse;
import com.tronhanh.dto.response.user.manage.UserManageResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.helper.SearchHelper;
import com.tronhanh.helper.UserManageHelper;
import com.tronhanh.repository.UserRepository;
import com.tronhanh.service.UserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManageServiceImpl implements UserManageService {

  private final UserRepository userRepository;
  private final SearchHelper searchHelper;
  private final UserManageHelper userManageHelper;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<UserManageResponse> searchUsers(
      PayloadRequest<UserManageRequest, UserSortRequest> request) {

    UserManageRequest searchData = request.getSearch();

    // 1. Sanitize search payload against XSS and Wildcard Injection
    searchHelper.sanitizeSearchPayload(searchData);

    // 2. Build Sort
    Sort sort = UserManageSortCondition.buildSort(request.getSort());

    // 3. Build Pageable
    Pageable pageable = PageItem.buildPageable(request.getPage(), sort);

    // 4. Query Database
    Page<UserEntity> userPage = userRepository.searchUsers(searchData, pageable);

    // 5. Map to Response
    return userManageHelper.buildUserManagePageResponse(userPage);
  }
}
