package com.tronhanh.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tronhanh.dto.request.profile.PatchUserProfileRequest;
import com.tronhanh.dto.request.profile.UpdateUserProfileRequest;
import com.tronhanh.dto.response.profile.UserProfileResponse;
import com.tronhanh.enums.Gender;
import com.tronhanh.service.UserProfileService;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit/MockMvc tests for UserProfileController endpoints.
 */
@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest
{

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock
  private UserProfileService userProfileService;

  @InjectMocks
  private UserProfileController userProfileController;

  private UUID userId;
  private UUID profileId;
  private UserProfileResponse mockResponse;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    userId = UUID.randomUUID();
    profileId = UUID.randomUUID();

    mockResponse = UserProfileResponse.builder()
        .userProfileId(profileId)
        .userId(userId)
        .email("test@tronhanh.vn")
        .firstName("Trong")
        .lastName("Le")
        .fullName("Le Thanh Trong")
        .phoneNumber("0987654321")
        .avatarUrl("https://s3.example.com/avatar.jpg")
        .dateOfBirth(LocalDate.of(2000, 1, 15))
        .gender(Gender.MALE)
        .bio("Student")
        .occupation("Software Engineer")
        .address("123 Nguyen Trai")
        .city("Ho Chi Minh City")
        .district("District 1")
        .ward("Ben Nghe")
        .build();
  }

  @Test
  @DisplayName("GET /api/v1/users/me/profile: 200 OK")
  void getCurrentUserProfile_Returns200() throws Exception {
    when(userProfileService.getCurrentUserProfile()).thenReturn(mockResponse);

    mockMvc.perform(get("/api/v1/users/me/profile")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.data.userId").value(userId.toString()))
        .andExpect(jsonPath("$.data.email").value("test@tronhanh.vn"))
        .andExpect(jsonPath("$.data.fullName").value("Le Thanh Trong"))
        .andExpect(jsonPath("$.data.occupation").value("Software Engineer"));
  }

  @Test
  @DisplayName("PUT /api/v1/users/me/profile: 200 OK")
  void updateCurrentUserProfile_Returns200() throws Exception {
    UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
        .avatarUrl("https://s3.example.com/avatar.jpg")
        .dateOfBirth(LocalDate.of(2000, 1, 15))
        .gender(Gender.MALE)
        .bio("Student")
        .occupation("Software Engineer")
        .address("123 Nguyen Trai")
        .city("Ho Chi Minh City")
        .district("District 1")
        .ward("Ben Nghe")
        .build();

    when(userProfileService.updateCurrentUserProfile(any(UpdateUserProfileRequest.class)))
        .thenReturn(mockResponse);

    mockMvc.perform(put("/api/v1/users/me/profile")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.data.userProfileId").value(profileId.toString()));
  }

  @Test
  @DisplayName("PATCH /api/v1/users/me/profile: 200 OK")
  void patchCurrentUserProfile_Returns200() throws Exception {
    PatchUserProfileRequest request = PatchUserProfileRequest.builder()
        .occupation("Senior Engineer")
        .build();

    when(userProfileService.patchCurrentUserProfile(any(PatchUserProfileRequest.class)))
        .thenReturn(mockResponse);

    mockMvc.perform(patch("/api/v1/users/me/profile")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.data.userProfileId").value(profileId.toString()));
  }

  @Test
  @DisplayName("GET /api/v1/users/{userId}/profile: 200 OK")
  void getUserProfileByAdmin_Returns200() throws Exception {
    when(userProfileService.getUserProfileByAdmin(eq(userId))).thenReturn(mockResponse);

    mockMvc.perform(get("/api/v1/users/{userId}/profile", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200))
        .andExpect(jsonPath("$.data.userId").value(userId.toString()));
  }

  @Test
  @DisplayName("DELETE /api/v1/users/{userId}/profile: 200 OK")
  void deleteUserProfileByAdmin_Returns200() throws Exception {
    doNothing().when(userProfileService).deleteUserProfileByAdmin(eq(userId));

    mockMvc.perform(delete("/api/v1/users/{userId}/profile", userId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(200));
  }
}