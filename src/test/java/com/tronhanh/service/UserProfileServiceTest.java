package com.tronhanh.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import com.tronhanh.entity.UserEntity;
import com.tronhanh.entity.UserProfileEntity;
import com.tronhanh.enums.Gender;
import com.tronhanh.enums.UserStatus;
import com.tronhanh.exception.HttpException;
import com.tronhanh.helper.UserProfileHelper;
import com.tronhanh.repository.UserProfileRepository;
import com.tronhanh.repository.UserRepository;
import com.tronhanh.service.impl.UserProfileServiceImpl;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Unit tests for UserProfileServiceImpl verifying all user and administrative operations.
 */
@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest
{

  @Mock
  private UserProfileRepository userProfileRepository;

  @Mock
  private UserRepository userRepository;

  @Spy
  private UserProfileHelper userProfileHelper = new UserProfileHelper();

  @InjectMocks
  private UserProfileServiceImpl userProfileService;

  private UserEntity mockUser;
  private UserProfileEntity mockProfile;
  private UUID userId;
  private UUID profileId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    profileId = UUID.randomUUID();

    mockUser = UserEntity.builder()
        .userId(userId)
        .email("test@tronhanh.vn")
        .firstName("Trong")
        .lastName("Le")
        .fullName("Le Thanh Trong")
        .phoneNumber("0987654321")
        .status(UserStatus.ACTIVE)
        .build();

    mockProfile = UserProfileEntity.builder()
        .userProfileId(profileId)
        .user(mockUser)
        .avatarUrl("https://s3.example.com/avatar.jpg")
        .dateOfBirth(LocalDate.of(2000, 1, 15))
        .gender(Gender.MALE)
        .bio("Student looking for a room")
        .occupation("Engineer")
        .address("123 Nguyen Trai")
        .city("Ho Chi Minh City")
        .district("District 1")
        .ward("Ben Nghe")
        .build();
    mockProfile.setIsDeleted(false);

    // Set mock user in SecurityContext
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(mockUser, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("getCurrentUserProfile: Success when profile exists")
  void getCurrentUserProfile_Success() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.of(mockProfile));

    UserProfileResponse response = userProfileService.getCurrentUserProfile();

    assertNotNull(response);
    assertEquals(profileId, response.getUserProfileId());
    assertEquals(userId, response.getUserId());
    assertEquals("test@tronhanh.vn", response.getEmail());
    assertEquals("Le Thanh Trong", response.getFullName());
    assertEquals("Engineer", response.getOccupation());
    assertEquals(Gender.MALE, response.getGender());
  }

  @Test
  @DisplayName("getCurrentUserProfile: Throws 404 when profile not found")
  void getCurrentUserProfile_NotFound_ThrowsHttpException() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.getCurrentUserProfile()
    );

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
  }

  @Test
  @DisplayName("getCurrentUserProfile: Throws 401 when unauthenticated")
  void getCurrentUserProfile_Unauthenticated_ThrowsHttpException() {
    SecurityContextHolder.clearContext();

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.getCurrentUserProfile()
    );

    assertEquals(HttpStatus.UNAUTHORIZED.value(), exception.getStatusCode());
  }

  @Test
  @DisplayName("updateCurrentUserProfile: Updates existing active profile")
  void updateCurrentUserProfile_WhenProfileExists_Updates() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.of(mockProfile));
    when(userProfileRepository.save(any(UserProfileEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
        .avatarUrl("https://s3.example.com/new-avatar.jpg")
        .dateOfBirth(LocalDate.of(1999, 5, 20))
        .gender(Gender.FEMALE)
        .bio("Updated bio")
        .occupation("Designer")
        .address("456 Le Duan")
        .city("Da Nang")
        .district("Hai Chau")
        .ward("Thach Thang")
        .build();

    UserProfileResponse response = userProfileService.updateCurrentUserProfile(request);

    assertNotNull(response);
    assertEquals("Designer", response.getOccupation());
    assertEquals(Gender.FEMALE, response.getGender());
    assertEquals("https://s3.example.com/new-avatar.jpg", response.getAvatarUrl());
    verify(userProfileRepository).save(mockProfile);
  }

  @Test
  @DisplayName("updateCurrentUserProfile: Creates new profile when none exists")
  void updateCurrentUserProfile_WhenProfileDoesNotExist_Creates() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.empty());
    when(userProfileRepository.save(any(UserProfileEntity.class))).thenAnswer(invocation -> {
      UserProfileEntity entity = invocation.getArgument(0);
      entity.setUserProfileId(UUID.randomUUID());
      return entity;
    });

    UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
        .avatarUrl("https://s3.example.com/new-avatar.jpg")
        .dateOfBirth(LocalDate.of(2001, 1, 1))
        .gender(Gender.OTHER)
        .bio("Brand new profile")
        .occupation("Architect")
        .address("789 Tran Phu")
        .city("Hanoi")
        .district("Ba Dinh")
        .ward("Dien Bien")
        .build();

    UserProfileResponse response = userProfileService.updateCurrentUserProfile(request);

    assertNotNull(response);
    assertEquals("Architect", response.getOccupation());
    assertEquals(Gender.OTHER, response.getGender());
    assertEquals(userId, response.getUserId());
    verify(userProfileRepository).save(any(UserProfileEntity.class));
  }

  @Test
  @DisplayName("patchCurrentUserProfile: Patches only non-null fields")
  void patchCurrentUserProfile_Success() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.of(mockProfile));
    when(userProfileRepository.save(any(UserProfileEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    PatchUserProfileRequest request = PatchUserProfileRequest.builder()
        .bio("Patched bio only")
        .occupation("Lead Engineer")
        .build();

    UserProfileResponse response = userProfileService.patchCurrentUserProfile(request);

    assertNotNull(response);
    assertEquals("Patched bio only", response.getBio());
    assertEquals("Lead Engineer", response.getOccupation());
    // Address was not patched, should remain old value
    assertEquals("123 Nguyen Trai", response.getAddress());
  }

  @Test
  @DisplayName("patchCurrentUserProfile: Throws 404 when profile not found")
  void patchCurrentUserProfile_NotFound_ThrowsHttpException() {
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.empty());

    PatchUserProfileRequest request = PatchUserProfileRequest.builder()
        .bio("Patched bio")
        .build();

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.patchCurrentUserProfile(request)
    );

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
  }

  @Test
  @DisplayName("getUserProfileByAdmin: Success when user and profile exist")
  void getUserProfileByAdmin_Success() {
    when(userRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(mockUser));
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.of(mockProfile));

    UserProfileResponse response = userProfileService.getUserProfileByAdmin(userId);

    assertNotNull(response);
    assertEquals(userId, response.getUserId());
    assertEquals("test@tronhanh.vn", response.getEmail());
  }

  @Test
  @DisplayName("getUserProfileByAdmin: Throws 404 when user does not exist")
  void getUserProfileByAdmin_UserNotFound_ThrowsHttpException() {
    when(userRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.getUserProfileByAdmin(userId)
    );

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
  }

  @Test
  @DisplayName("getUserProfileByAdmin: Throws 404 when profile does not exist")
  void getUserProfileByAdmin_ProfileNotFound_ThrowsHttpException() {
    when(userRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(mockUser));
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.getUserProfileByAdmin(userId)
    );

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
  }

  @Test
  @DisplayName("deleteUserProfileByAdmin: Soft-deletes profile successfully")
  void deleteUserProfileByAdmin_Success() {
    when(userRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.of(mockUser));
    when(userProfileRepository.findByUserIdActive(userId)).thenReturn(Optional.of(mockProfile));

    userProfileService.deleteUserProfileByAdmin(userId);

    assertTrue(mockProfile.getIsDeleted());
    verify(userProfileRepository).save(mockProfile);
  }

  @Test
  @DisplayName("deleteUserProfileByAdmin: Throws 404 when user does not exist")
  void deleteUserProfileByAdmin_UserNotFound_ThrowsHttpException() {
    when(userRepository.findByUserIdAndIsDeletedFalse(userId)).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () ->
        userProfileService.deleteUserProfileByAdmin(userId)
    );

    assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
  }
}